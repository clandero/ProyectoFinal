package com.example.proyectofinal;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListadoAlertaAdapter extends RecyclerView.Adapter<ListadoAlertaAdapter.ListadoAlertaViewHolder> {
    private ArrayList<ListadoAlertaItem> mList;
    public static class ListadoAlertaViewHolder extends RecyclerView.ViewHolder{
        public TextView mNombre;
        public TextView mDetalle;
        public TextView mDireccion;
        public TextView mAsistentesSolicitados;
        public TextView mAsistencia;
        public Button mAsistir;
        public Button mRechazar;
        String nombre_bombero;
        Integer id_alerta;
        SharedPreferences settings;
        public ListadoAlertaViewHolder(@NonNull final View itemView) {
            super(itemView);
            mNombre = itemView.findViewById(R.id.nombre_alerta);
            mDetalle = itemView.findViewById(R.id.detalle_alerta);
            mDireccion = itemView.findViewById(R.id.direccion_alerta);
            mAsistentesSolicitados = itemView.findViewById(R.id.asistentes_solicitados);
            mAsistencia = itemView.findViewById(R.id.asistencia);
            mAsistir = itemView.findViewById(R.id.boton_asistir);
            settings = itemView.getContext().getSharedPreferences("preferences",0);
            nombre_bombero = settings.getString("nombre_bombero", " ");
            id_alerta = settings.getInt("id_alerta",0);
            mAsistir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAsistir.setVisibility(View.INVISIBLE);
                    mRechazar.setVisibility(View.INVISIBLE);
                    mAsistencia.setVisibility(View.VISIBLE);
                    mAsistencia.setText("Asistencia confirmada");
                    mAsistencia.setBackgroundColor(Color.parseColor("#B6F861"));


                }
            });
            mRechazar = itemView.findViewById(R.id.boton_rechazar);
            mRechazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAsistir.setVisibility(View.INVISIBLE);
                    mRechazar.setVisibility(View.INVISIBLE);
                    mAsistencia.setVisibility(View.VISIBLE);
                    mAsistencia.setText("Asistencia rechazada");
                    mAsistencia.setBackgroundColor(Color.parseColor("#F37E7E"));

                }
            });
        }
        private void add_bombero_y_alerta(final Context context, final String nombre_bombero, final Integer id_alerta) throws JSONException {
            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject x = new JSONObject();
            x.put("nombre_bombero",nombre_bombero);
            x.put("id_alerta",id_alerta);
            final JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://clandero.pythonanywhere.com/add_bombero_alerta",
                    x,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Rest Response", response.toString());
                            try {
                                if(response.getInt("rsp")==1){
                                    //USER HAS LOGGED IN
                                    SharedPreferences settings = context.getSharedPreferences("preferences",0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("nombre_bombero",nombre_bombero);
                                    editor.putInt("id_alerta",id_alerta);
                                    editor.commit();
                                }
                                else if(response.getInt("rsp")==-1){
                                    Toast.makeText(context, "Fallo post", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(context, "Fallo post", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Rest Response", error.toString());
                        }
                    });

            requestQueue.add(objectRequest);

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
        holder.mNombre.setText(current.getFecha().substring(8,10)+"/"+current.getFecha().substring(5,7)+" "+current.getNombre()+": "+current.getTipo()+" "+current.getFecha().substring(11,16));
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

