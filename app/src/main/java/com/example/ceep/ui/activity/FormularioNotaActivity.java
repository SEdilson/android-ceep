package com.example.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.model.Cores;
import com.example.ceep.model.Nota;
import com.example.ceep.ui.recycler.adapter.ListaCoresAdapter;

import java.util.Arrays;
import java.util.List;

import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CHAVE_NOTA;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.CHAVE_POSICAO;
import static com.example.ceep.ui.activity.ListaNotasActivityConstantes.POSICAO_INVALIDA;


public class FormularioNotaActivity extends AppCompatActivity {

    public static final String CRIA_NOTA_TITULO_APPBAR = "Cria Nota";
    public static final String ALTERA_NOTA_TITULO_APPBAR = "Altera Nota";
    private Nota nota;
    private EditText campoTitulo;
    private EditText campoDescricao;
    private ConstraintLayout formNotaLayout = findViewById(R.id.activity_form_nota_layout);
    private List<Cores> cores = Arrays.asList(Cores.values());
//    private RoomNotaDAO dao;
    private int posicaoRecebida = POSICAO_INVALIDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(CRIA_NOTA_TITULO_APPBAR);

        inicializaCampos();
        vinculaValores();

        configuraAdapterListaCoresNota();
    }

    private void configuraAdapterListaCoresNota() {
        ListaCoresAdapter adapter = new ListaCoresAdapter(this, cores);
        RecyclerView viewCoresFormNota = findViewById(R.id.activity_form_cores);
        viewCoresFormNota.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListaCoresAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int posicao) {
                configuraMudancaDeCorDoFormNota(posicao);
            }
        });
    }

    private void configuraMudancaDeCorDoFormNota(int posicao) {
        String corRetornada = cores.get(posicao).getCor();
        int corParseada = Color.parseColor(corRetornada);
        formNotaLayout.setBackgroundColor(corParseada);
    }

    private void vinculaValores() {
        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA)) {
            setTitle(ALTERA_NOTA_TITULO_APPBAR);
            nota = (Nota) dadosRecebidos.
                    getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            campoTitulo.setText(nota.getTitulo());
            campoDescricao.setText(nota.getDescricao());
        } else {
            nota = new Nota();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_form_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(ehMenuItemInsereNota(itemId)) {
            preencheNota();
            resultadoInsercaoNota();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean ehMenuItemInsereNota(int item) {
        return item == R.id.activity_formulario_aluno_menu_salvar;
    }

    private void resultadoInsercaoNota() {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    private void inicializaCampos() {
        campoTitulo = findViewById(R.id.activity_form_nota_titulo);
        campoDescricao = findViewById(R.id.activity_form_nota_descricao);
    }

    private void preencheNota() {
        String titulo = campoTitulo.getText().toString();
        String descricao = campoDescricao.getText().toString();

        nota = new Nota(titulo, descricao);
    }
}
