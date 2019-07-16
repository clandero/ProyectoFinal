package com.example.proyectofinal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {
    private ArrayList<HistoricoItem> mList;
    public static class HistoricoViewHolder extends RecyclerView.ViewHolder{
        //LO QUE SEA QUE ES EL ELEMENTO PARA EL MAPA :^)
        public TextView mTitle;
        public TextView mType;
        public TextView mContent;
        public HistoricoViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.historico_nombre_alerta);
            mType = itemView.findViewById(R.id.historico_tipo_alerta);
            mContent = itemView.findViewById(R.id.historico_detalle);
        }
    }

    public HistoricoAdapter(ArrayList<HistoricoItem> list){
        mList = list;
    }

    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historico_item, parent,false);
        HistoricoViewHolder lcvh = new HistoricoViewHolder(v);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(HistoricoViewHolder holder, int i) {
        HistoricoItem current = mList.get(i);
        holder.mTitle.setText(current.getNombre());
        holder.mType.setText(current.getTipoalerta());
        holder.mContent.setText(current.getDetalle());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
