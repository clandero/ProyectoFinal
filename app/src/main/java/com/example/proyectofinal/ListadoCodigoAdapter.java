package com.example.proyectofinal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListadoCodigoAdapter extends RecyclerView.Adapter<ListadoCodigoAdapter.ListadoCodigoViewHolder> {
    private ArrayList<ListadoCodigoItem> mList;
    public static class ListadoCodigoViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView;
        public ListadoCodigoViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);
        }
    }

    public ListadoCodigoAdapter(ArrayList<ListadoCodigoItem> list){
        mList = list;
    }

    @Override
    public ListadoCodigoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_codigo_item, parent,false);
        ListadoCodigoViewHolder lcvh = new ListadoCodigoViewHolder(v);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(ListadoCodigoViewHolder holder, int i) {
        ListadoCodigoItem current = mList.get(i);
        holder.mImageView.setImageResource(current.getImageResource());
        holder.mTextView.setText(current.getTextResource());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
