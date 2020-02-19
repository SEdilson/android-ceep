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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.ceep.R;
import com.example.ceep.model.Nota;
import com.example.ceep.repository.NotaRepository;
import com.example.ceep.ui.recycler.adapter.ListaNotasAdapter;
import com.example.ceep.ui.recycler.helper.callback.NotaItemTouchHelperCallback;

import java.util.List;

import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CHAVE_NOTA;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CHAVE_NOTA_ID;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.LAYOUT_APLICADO;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.LAYOUT_PADRAO;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.LAYOUT_PREFERENCES_CHAVE;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.LINEAR_LAYOUT;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.STAGGERED_LAYOUT;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.TITULO_NOTAS_APPBAR;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;
    private SharedPreferences layoutPreferences;
    private String layoutDoRecyclerView;
    private RecyclerView listaDeNotas;
    private NotaRepository repository;
    private boolean isVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_NOTAS_APPBAR);
        layoutPreferences = getSharedPreferences(LAYOUT_PREFERENCES_CHAVE, MODE_PRIVATE);
        listaDeNotas = findViewById(R.id.activity_lista_notas_recyclerview);
        repository = new NotaRepository(this);

        inicializaFormNota();
        configuraListaDeNotas();
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

        layoutDoRecyclerView = layoutPreferences.getString(LAYOUT_APLICADO, LAYOUT_PADRAO);

        if(layoutDoRecyclerView.equals(STAGGERED_LAYOUT)) {
            itemMenuLinearLayout.setVisible(isVisible);
            itemMenuStaggeredLayout.setVisible(!isVisible);
        } else {
            itemMenuStaggeredLayout.setVisible(isVisible);
            itemMenuLinearLayout.setVisible(!isVisible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = layoutPreferences.edit();

        switch (item.getItemId()) {
            case R.id.activity_lista_notas_opcao_linear_layout:
                atribuiValorAPreference(editor, LINEAR_LAYOUT);
                atribuiPreferencesAoLayout();
                break;

            case R.id.activity_lista_notas_opcao_staggered_layout:
                atribuiValorAPreference(editor, STAGGERED_LAYOUT);
                atribuiPreferencesAoLayout();
                break;

            case R.id.activity_lista_notas_feedback:
                configuraFormularioDeFeedback();
        }

        return super.onOptionsItemSelected(item);
    }

    private void configuraFormularioDeFeedback() {
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
        finish();
    }

    private void atribuiValorAPreference(SharedPreferences.Editor editor, String layout) {
        editor.putString(LAYOUT_APLICADO, layout);
        editor.apply();
    }

    private void atribuiPreferencesAoLayout() {
        layoutDoRecyclerView = layoutPreferences.getString(LAYOUT_APLICADO, LAYOUT_PADRAO);

        if(layoutDoRecyclerView.equals(STAGGERED_LAYOUT)) {
            listaDeNotas.setLayoutManager(new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL));
        } else {
            listaDeNotas.setLayoutManager(new LinearLayoutManager(this));
        }

        invalidateOptionsMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(ehResultadoInsereNota(requestCode, data)) {
            if(resultadoOK(resultCode)) {
                Nota notaRetornada = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                repository.salvaNota(notaRetornada, new NotaRepository.DadosCarregadosCallback<Nota>() {
                    @Override
                    public void quandoSucesso(Nota resposta) {
                        adapter.adiciona(resposta);
                    }

                    @Override
                    public void quandoErro(String erro) {
                        Toast.makeText(ListaNotasActivity.this,
                                "Não foi possível salvar a nota", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        if(ehResultadoAlteraNota(requestCode, data)) {
            if(resultadoOK(resultCode)) {
                Nota notaRetornada = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                repository.atualizaNota(notaRetornada, new NotaRepository.DadosCarregadosCallback<Nota>() {
                    @Override
                    public void quandoSucesso(Nota resposta) {
                        adapter.altera(resposta);
                    }

                    @Override
                    public void quandoErro(String erro) {
                        Toast.makeText(ListaNotasActivity.this,
                                "Não foi possível alterar a nota", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNotaComId(data);
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

    private boolean temNotaComId(@Nullable Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA_ID);
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

    private void configuraListaDeNotas() {
        configuraAdapter(listaDeNotas);
        configuraItemTouchHelper(listaDeNotas);
        repository.buscaNotas(new NotaRepository.DadosCarregadosCallback<List<Nota>>() {
            @Override
            public void quandoSucesso(List<Nota> resposta) {
                adapter.todasAsNotas(resposta);
            }

            @Override
            public void quandoErro(String erro) {
                Toast.makeText(ListaNotasActivity.this,
                        "Não foi possível carregar os dados", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configuraItemTouchHelper(RecyclerView listaDeNotas) {
        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter, repository));
        itemTouchHelper.attachToRecyclerView(listaDeNotas);
    }

    private void configuraAdapter(RecyclerView listaDeNotas) {
        adapter = new ListaNotasAdapter(this, this::vaiParaFormActivityAlteraNota);
        listaDeNotas.setAdapter(adapter);
    }

    private void vaiParaFormActivityAlteraNota(Nota nota) {
        Intent inicializaFormDeAtualizacao = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        inicializaFormDeAtualizacao.putExtra(CHAVE_NOTA, nota);
        inicializaFormDeAtualizacao.putExtra(CHAVE_NOTA_ID, nota);
        startActivityForResult(inicializaFormDeAtualizacao, CODIGO_REQUISICAO_ALTERA_NOTA);
    }
}
