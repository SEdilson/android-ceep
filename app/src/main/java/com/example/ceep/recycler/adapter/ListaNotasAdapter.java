package com.example.ceep.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.model.Nota;

import java.util.List;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.ListaNotasHolder> {

    private final List<Nota> notas;
    private final Context context;

    public ListaNotasAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
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

    public class ListaNotasHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;

        public ListaNotasHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
        }

        public void vincula(Nota nota) {
            preencheCampos(nota);
        }

        private void preencheCampos(Nota nota) {
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
        }
    }
}
