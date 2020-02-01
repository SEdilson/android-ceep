package com.example.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.dao.NotaDAO;
import com.example.ceep.model.Nota;
import com.example.ceep.ui.recycler.adapter.ListaNotasAdapter;
import com.example.ceep.ui.recycler.adapter.listener.OnItemClickListener;

import java.util.List;

import static com.example.ceep.ui.activity.Codigos.CODIGO_REQUISICAO_INSERE_NOTA;
import static com.example.ceep.ui.activity.Codigos.CODIGO_RESULTADO_INSERE_NOTA;
import static com.example.ceep.ui.activity.Codigos.RESULTADO_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {

    private NotaDAO dao = new NotaDAO();
    private ListaNotasAdapter adapter;
    private List<Nota> notas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        inicializaFormNota();
        inicializaNotas();
        configuraListaDeNotas(notas);
    }

    private void inicializaNotas() {
        notas = dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(ehResultadoComNota(requestCode, resultCode, data)) {
            Nota notaRetornada = (Nota) data.getSerializableExtra(RESULTADO_NOTA_CRIADA);
            dao.insere(notaRetornada);
            adapter.adiciona(notaRetornada);
        }

        if(requestCode == 2 &&
                resultCode == CODIGO_RESULTADO_INSERE_NOTA &&
                temNota(data)) {
            Nota notaRetornada = (Nota) data.getSerializableExtra(RESULTADO_NOTA_CRIADA);
            int posicaoRecebida = data.getIntExtra("posicao", -1);
            dao.altera(posicaoRecebida, notaRetornada);
            adapter.altera(posicaoRecebida, notaRetornada);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean ehResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return ehCodigoRequisicao(requestCode) && ehCodigoResultado(resultCode) && temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra(RESULTADO_NOTA_CRIADA);
    }

    private boolean ehCodigoResultado(int resultCode) {
        return resultCode == CODIGO_RESULTADO_INSERE_NOTA;
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
    }

    private void configuraAdapter(List<Nota> notas, RecyclerView listaDeNotas) {
        adapter = new ListaNotasAdapter(this, notas);
        listaDeNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                Intent inicializaFormDeAtualizacao = new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
                inicializaFormDeAtualizacao.putExtra(RESULTADO_NOTA_CRIADA, nota);
                inicializaFormDeAtualizacao.putExtra("posicao", posicao);
                startActivityForResult(inicializaFormDeAtualizacao, 2);
            }
        });
    }
}
