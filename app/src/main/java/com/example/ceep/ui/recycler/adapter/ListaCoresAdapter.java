package com.example.ceep.ui.recycler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.model.Cores;

import java.util.List;

public class ListaCoresAdapter extends RecyclerView.Adapter<ListaCoresAdapter.ListaCoresHolder> {

    private List<Cores> cores;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public ListaCoresAdapter(Context context, List<Cores> cores) {
        this.context = context;
        this.cores = cores;
    }

    @NonNull
    @Override
    public ListaCoresAdapter.ListaCoresHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_cor_nota, viewGroup, false);
        return new ListaCoresHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaCoresHolder holder, int position) {
        Cores corRetornada = this.cores.get(position);
        holder.vincula(corRetornada);
    }

    @Override
    public int getItemCount() {
        return cores.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ListaCoresHolder extends RecyclerView.ViewHolder {

        private final ImageView corNota;
        private Cores cor;

        public ListaCoresHolder(@NonNull View itemView) {
            super(itemView);
            corNota = itemView.findViewById(R.id.imagem_cor_nota);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(getAdapterPosition());
                }
            });
        }

        public void vincula(Cores cor) {
            this.cor = cor;
            configuraDrawableDeCores(cor);
        }

        private void configuraDrawableDeCores(Cores cor) {
            int color = Color.parseColor(cor.getCor());
            Drawable drawableCor = context.getResources()
                    .getDrawable(R.drawable.shape_imagem_cores);
            corNota.setBackground(drawableCor);
            drawableCor.setColorFilter(color, PorterDuff.Mode.SRC_OVER);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int posicao);
    }
}
