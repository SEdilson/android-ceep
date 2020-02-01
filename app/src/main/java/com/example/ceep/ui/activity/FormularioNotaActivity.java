package com.example.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ceep.R;
import com.example.ceep.model.Nota;

import static com.example.ceep.ui.activity.Codigos.RESULTADO_NOTA_CRIADA;
import static com.example.ceep.ui.activity.Codigos.CODIGO_RESULTADO_INSERE_NOTA;


public class FormularioNotaActivity extends AppCompatActivity {

    private Nota nota;
    private EditText campoTitulo;
    private EditText campoDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        inicializaCampos();
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
        resultadoInsercao.putExtra(RESULTADO_NOTA_CRIADA, nota);
        setResult(CODIGO_RESULTADO_INSERE_NOTA, resultadoInsercao);
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
