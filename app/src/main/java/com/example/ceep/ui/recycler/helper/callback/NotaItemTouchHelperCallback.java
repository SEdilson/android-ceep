package com.example.ceep.ui.recycler.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.model.Nota;
import com.example.ceep.repository.NotaRepository;
import com.example.ceep.ui.recycler.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;
    private final NotaRepository repository;
    private Nota nota;

    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter, NotaRepository repository) {
        this.adapter = adapter;
        this.repository = repository;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int movimentosDeDeslize = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int movimentosDeArratar = ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(movimentosDeArratar, movimentosDeDeslize);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        trocaPosicaoDasNotas(posicaoInicial, posicaoFinal);
        return true;
    }

    private void trocaPosicaoDasNotas(int posicaoInicial, int posicaoFinal) {
        adapter.troca(posicaoInicial, posicaoFinal);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int notaPosicao = viewHolder.getAdapterPosition();
        Nota notaRetornada = adapter.retornaNotaNaPosicao(notaPosicao);
        removeNota(notaRetornada);
    }

    private void removeNota(Nota nota) {
        this.nota = nota;
        repository.removeNota(nota, new NotaRepository.DadosCarregadosCallback<Void>() {
            @Override
            public void quandoSucesso(Void resposta) {
                adapter.remove(nota);
            }

            @Override
            public void quandoErro(String erro) {

            }
        });
    }
}
