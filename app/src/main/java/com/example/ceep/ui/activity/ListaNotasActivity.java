package com.example.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.ceep.R;
import com.example.ceep.dao.NotaDAO;
import com.example.ceep.model.Nota;
import com.example.ceep.ui.recycler.adapter.ListaNotasAdapter;
import com.example.ceep.ui.recycler.adapter.listener.OnItemClickListener;
import com.example.ceep.ui.recycler.helper.callback.NotaItemTouchHelperCallback;

import java.util.List;

import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CHAVE_NOTA;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CHAVE_POSICAO;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.LAYOUT_APLICADO;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.LAYOUT_PREFERENCES_CHAVE;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.LINEAR_LAYOUT;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.POSICAO_INVALIDA;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.STAGGERED_LAYOUT;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.TITULO_NOTAS_APPBAR;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;
    private SharedPreferences layoutPreferences;
    private String layoutDoRecyclerView;
    private boolean isVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_NOTAS_APPBAR);
        List<Nota> notas = todasAsNotas();

        inicializaFormNota();
        configuraListaDeNotas(notas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.activity_lista_notas_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        layoutPreferences = getSharedPreferences(LAYOUT_PREFERENCES_CHAVE, MODE_PRIVATE);
        layoutDoRecyclerView = layoutPreferences.getString(LAYOUT_APLICADO, LINEAR_LAYOUT);

        menu.findItem(R.id.activity_lista_notas_feedback).setVisible(isVisible);

        MenuItem itemMenuLinearLayout =
                menu.findItem(R.id.activity_lista_notas_opcao_linear_layout);

        MenuItem itemMenuStaggeredLayout =
                menu.findItem(R.id.activity_lista_notas_opcao_staggered_layout);

        configuraExibicaoOptionsMenu(itemMenuLinearLayout, itemMenuStaggeredLayout);

        return true;
    }

    private void configuraExibicaoOptionsMenu(MenuItem itemMenuLinearLayout,
                                              MenuItem itemMenuStaggeredLayout) {

        if(layoutDoRecyclerView.equals(LINEAR_LAYOUT)) {
            itemMenuStaggeredLayout.setVisible(isVisible);
            itemMenuLinearLayout.setVisible(!isVisible);
        }
        if(layoutDoRecyclerView.equals(STAGGERED_LAYOUT)) {
            itemMenuLinearLayout.setVisible(isVisible);
            itemMenuStaggeredLayout.setVisible(!isVisible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = layoutPreferences.edit();

        switch (item.getItemId()) {
            case R.id.activity_lista_notas_opcao_linear_layout:
                layoutPreferences = getSharedPreferences(LAYOUT_PREFERENCES_CHAVE, MODE_PRIVATE);
                editor.putString(LAYOUT_APLICADO, LINEAR_LAYOUT);
                editor.apply();

                atribuiPreferencesAoLayout();
                break;

            case R.id.activity_lista_notas_opcao_staggered_layout:
                editor.putString(LAYOUT_APLICADO, STAGGERED_LAYOUT);
                editor.apply();

                atribuiPreferencesAoLayout();
                break;

            case R.id.activity_lista_notas_feedback:
                Intent intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void atribuiPreferencesAoLayout() {
        layoutPreferences = getSharedPreferences(LAYOUT_PREFERENCES_CHAVE, MODE_PRIVATE);
        layoutDoRecyclerView = layoutPreferences.getString(LAYOUT_APLICADO, LINEAR_LAYOUT);

        if(layoutDoRecyclerView.equals(STAGGERED_LAYOUT)) {
            configuraRecyclerViewLayoutManager(new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL));
        }
        if(layoutDoRecyclerView.equals(LINEAR_LAYOUT)) {
            configuraRecyclerViewLayoutManager(new LinearLayoutManager(this));
        }

        invalidateOptionsMenu();
    }

    private void configuraRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        RecyclerView viewListaNotas = findViewById(R.id.activity_lista_notas_recyclerview);
        viewListaNotas.setLayoutManager(layoutManager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(ehResultadoInsereNota(requestCode, data)) {
            if(resultadoOK(resultCode)) {
                Nota notaRetornada = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                insere(notaRetornada);
            }
        }

        if(ehResultadoAlteraNota(requestCode, data)) {
            if(resultadoOK(resultCode)) {
                Nota notaRetornada = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if(posicaoRecebida > POSICAO_INVALIDA) {
                    altera(notaRetornada, posicaoRecebida);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public List<Nota> todasAsNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    private void insere(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean ehResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return ehCodigoRequisicao(requestCode) && temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOK(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicao(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void inicializaFormNota() {
        TextView botaoInsereNota = findViewById(R.id.activity_lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicializaViewFormNota();
            }
        });
    }

    private void inicializaViewFormNota() {
        Intent resultadoEsperado = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        startActivityForResult(resultadoEsperado, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private void configuraListaDeNotas(List<Nota> notas) {
        RecyclerView listaDeNotas = findViewById(R.id.activity_lista_notas_recyclerview);
        configuraAdapter(notas, listaDeNotas);
        configuraItemTouchHelper(listaDeNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaDeNotas) {
        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaDeNotas);
    }

    private void configuraAdapter(List<Nota> notas, RecyclerView listaDeNotas) {
        adapter = new ListaNotasAdapter(this, notas);
        listaDeNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormActivityAlteraNota(nota, posicao);
            }
        });
    }

    private void vaiParaFormActivityAlteraNota(Nota nota, int posicao) {
        Intent inicializaFormDeAtualizacao = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        inicializaFormDeAtualizacao.putExtra(CHAVE_NOTA, nota);
        inicializaFormDeAtualizacao.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(inicializaFormDeAtualizacao, CODIGO_REQUISICAO_ALTERA_NOTA);
    }
}
