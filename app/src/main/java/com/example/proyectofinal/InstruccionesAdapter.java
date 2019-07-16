package com.example.proyectofinal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class InstruccionesAdapter extends RecyclerView.Adapter<InstruccionesAdapter.InstruccionesViewHolder> {
    private ArrayList<InstruccionesItem> mList;
    public static class InstruccionesViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTitle;
        public TextView mContent;
        public InstruccionesViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            mTitle = itemView.findViewById(R.id.instruccion_title);
            mContent = itemView.findViewById(R.id.instruccion_content);
        }
    }

    public InstruccionesAdapter(ArrayList<InstruccionesItem> list){
        mList = list;
    }

    @Override
    public InstruccionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.instrucciones_item, parent,false);
        InstruccionesViewHolder lcvh = new InstruccionesViewHolder(v);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(InstruccionesViewHolder holder, int i) {
        InstruccionesItem current = mList.get(i);
        holder.mImageView.setImageResource(current.getImageResource());
        holder.mTitle.setText(current.getTitle());
        holder.mContent.setText(current.getContent());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
