package com.example.proyectofinal;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListadoAlertaAdapter extends RecyclerView.Adapter<ListadoAlertaAdapter.ListadoAlertaViewHolder> {
    private ArrayList<ListadoAlertaItem> mList;
    public static class ListadoAlertaViewHolder extends RecyclerView.ViewHolder{
        public TextView mNombre;
        public TextView mDetalle;
        public TextView mDireccion;
        public TextView mAsistentesSolicitados;
        public ListadoAlertaViewHolder(@NonNull View itemView) {
            super(itemView);
            mNombre = itemView.findViewById(R.id.nombre_alerta);
            mDetalle = itemView.findViewById(R.id.detalle_alerta);
            mDireccion = itemView.findViewById(R.id.direccion_alerta);
            mAsistentesSolicitados = itemView.findViewById(R.id.asistentes_solicitados);
        }
    }

    public ListadoAlertaAdapter(ArrayList<ListadoAlertaItem> list){
        mList = list;
    }

    @Override
    public ListadoAlertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_alerta_item, parent,false);
        ListadoAlertaViewHolder lcvh = new ListadoAlertaViewHolder(v);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(ListadoAlertaViewHolder holder, int i) {
        ListadoAlertaItem current = mList.get(i);
        holder.mNombre.setText(current.getNombre()+": "+current.getTipo());
        holder.mDetalle.setText(current.getDetalle());
        holder.mDireccion.setText("Dirección: "+current.getDireccion());
        holder.mAsistentesSolicitados.setText("Asistentes : "+current.getAsistentesActuales()+"/"+current.getAsistentesSolicitados());
        Log.d("EN LISTADO ALERTA",current.getNombre());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



}

