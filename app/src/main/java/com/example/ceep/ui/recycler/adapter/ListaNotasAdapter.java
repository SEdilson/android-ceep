package com.example.ceep.ui.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.model.Nota;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.ListaNotasHolder> {

    private final List<Nota> notas = new ArrayList<>();
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public ListaNotasAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListaNotasAdapter.ListaNotasHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_nota, viewGroup, false);
        return new ListaNotasHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaNotasAdapter.ListaNotasHolder holder, int position) {
        Nota notaRetornada = notas.get(position);
        holder.vincula(notaRetornada);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void adiciona(Nota nota) {
        notas.add(nota);
        notifyDataSetChanged();
    }

    public void todasAsNotas(List<Nota> notas) {
        notifyItemRangeRemoved(0, this.notas.size());
        this.notas.clear();
        this.notas.addAll(notas);
        this.notifyItemRangeInserted(0, this.notas.size());
    }

    public void altera(Nota nota) {
        this.notas.set(nota.getId(), nota);
        notifyItemChanged(nota.getPosicao());
    }

//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }

    public void remove(Nota nota) {
        notas.remove(nota);
        notifyItemRemoved(nota.getId());
    }

    public void troca(int posicaoInicial, int posicaoFinal) {
        Collections.swap(notas, posicaoInicial, posicaoFinal);
        notifyItemMoved(posicaoInicial, posicaoFinal);
    }

    public class ListaNotasHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;
        private final ConstraintLayout itemNotaLayout;
        private Nota nota;

        public ListaNotasHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
            itemNotaLayout = itemView.findViewById(R.id.item_nota_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(nota);
                }
            });
        }

        public void vincula(Nota nota) {
            this.nota = nota;
            preencheNota(nota);
        }

        private void preencheNota(Nota nota) {
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
            itemNotaLayout.setBackgroundColor(nota.getCor());
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(Nota nota);
    }
}
