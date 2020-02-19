package com.example.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

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


public class FormularioNotaActivity extends AppCompatActivity {

    public static final String CRIA_NOTA_TITULO_APPBAR = "Cria Nota";
    public static final String ALTERA_NOTA_TITULO_APPBAR = "Altera Nota";
    private Nota nota;
    private EditText campoTitulo;
    private EditText campoDescricao;
    private List<Cores> cores = Arrays.asList(Cores.values());
    private ConstraintLayout formNotaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(CRIA_NOTA_TITULO_APPBAR);

        inicializaNota();
        vinculaValores();

        configuraAdapterListaCoresNota();
    }

    private void vinculaValores() {
        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA)) {
            setTitle(ALTERA_NOTA_TITULO_APPBAR);
            nota = (Nota) dadosRecebidos.
                    getSerializableExtra(CHAVE_NOTA);
            campoTitulo.setText(nota.getTitulo());
            campoDescricao.setText(nota.getDescricao());
            formNotaLayout.setBackgroundColor(nota.getCor());
        } else {
            nota = new Nota();
        }
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
        nota.setCor(corParseada);
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
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    private void inicializaNota() {
        campoTitulo = findViewById(R.id.activity_form_nota_titulo);
        campoDescricao = findViewById(R.id.activity_form_nota_descricao);
        formNotaLayout = findViewById(R.id.activity_form_nota_layout);
    }

    private void preencheNota() {
        String titulo = campoTitulo.getText().toString();
        String descricao = campoDescricao.getText().toString();
        int corNota = configuraPreenchimentoDaCor();

        nota = new Nota(titulo, descricao, corNota);
    }

    private int configuraPreenchimentoDaCor() {
        ColorDrawable backgroundNota = (ColorDrawable) formNotaLayout.getBackground();
        return backgroundNota.getColor();
    }
}
