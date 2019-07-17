package com.example.proyectofinal;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class ListadoAlertaAdapter extends RecyclerView.Adapter<ListadoAlertaAdapter.ListadoAlertaViewHolder> {
    private ArrayList<ListadoAlertaItem> mList;
    Vector<Integer> alertas_id;
    Vector<String> alertas_nombre;
    Context context;
    public static class ListadoAlertaViewHolder extends RecyclerView.ViewHolder{
        public TextView mNombre;
        public TextView mDetalle;
        public TextView mDireccion;
        public TextView mAsistentesSolicitados;
        public TextView mAsistencia;
        public Button mAsistir;
        SharedPreferences settings;
        public ListadoAlertaViewHolder(@NonNull final View itemView) {
            super(itemView);
            mNombre = itemView.findViewById(R.id.nombre_alerta);
            mDetalle = itemView.findViewById(R.id.detalle_alerta);
            mDireccion = itemView.findViewById(R.id.direccion_alerta);
            mAsistentesSolicitados = itemView.findViewById(R.id.asistentes_solicitados);
            mAsistencia = itemView.findViewById(R.id.asistencia);
            mAsistir = itemView.findViewById(R.id.boton_asistir);

        }
        private void add_bombero_y_alerta(final Context context, final String nombre_bombero, final Integer id_alerta) throws JSONException {
            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject x = new JSONObject();
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
                                    editor.putString("nombre",nombre_bombero);
                                    editor.putInt("id",id_alerta);
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
    public void fetchAlert(final Context context, final Integer k, final String user){
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://clandero.pythonanywhere.com/get_all/"+k,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Rest Response1", response.toString());
                        try {
                            for( int i = 0; i < response.length() ; i++){
                                // Loop through the array elements
                                // Get current json object
                                JSONObject x = response.getJSONObject(i);
                                // Get the current student (json object) data
                                if(x.getString("username").equals(user)){
                                    alertas_nombre.add(x.getString("username"));
                                    Log.d("SON IGUALES",user);
                                }
                            }
                            if(alertas_nombre.size()==0) {
                                alertas_nombre.clear();
                                Log.d("SON DISTINTOS",user);
                                final RequestQueue requestQueue2 = Volley.newRequestQueue(context);
                                JSONObject x = new JSONObject();
                                try {
                                    x.put("nombre_bombero",user);
                                    x.put("id_alerta",k);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                final JsonObjectRequest objectRequest2 = new JsonObjectRequest(
                                        Request.Method.POST,
                                        "http://clandero.pythonanywhere.com/add_bombero_alerta",
                                        x,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.e("Rest Response", response.toString());
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("Rest Response", error.toString());
                                            }
                                        });

                                requestQueue2.add(objectRequest2);
                            }
                        }catch (JSONException e){
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

    public ListadoAlertaAdapter(ArrayList<ListadoAlertaItem> list, Context pcontext){
        mList = list;
        alertas_id = new Vector<Integer>();
        alertas_nombre = new Vector<String>();
        context = pcontext;
    }

    @Override
    public ListadoAlertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_alerta_item, parent,false);
        ListadoAlertaViewHolder lcvh = new ListadoAlertaViewHolder(v);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(final ListadoAlertaViewHolder holder, final int i) {
        final ListadoAlertaItem current = mList.get(i);
        final Class contexto = this.getClass();
        holder.mNombre.setText(current.getFecha().substring(8,10)+"/"+current.getFecha().substring(5,7)+" "+current.getNombre()+": "+current.getTipo()+" "+current.getFecha().substring(11,16));
        holder.mDetalle.setText(current.getDetalle());
        holder.mDireccion.setText("Dirección: "+current.getDireccion());
        holder.mAsistentesSolicitados.setText("Asistentes : "+current.getAsistentesActuales()+"/"+current.getAsistentesSolicitados());
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://clandero.pythonanywhere.com/get_all/"+current.getId_alerta(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Rest Response1", response.toString());
                        try {
                            for( int i = 0; i < response.length() ; i++){
                                // Loop through the array elements
                                // Get current json object
                                JSONObject x = response.getJSONObject(i);
                                // Get the current student (json object) data
                                if(x.getString("username").equals(current.getUsername())){
                                    holder.mAsistir.setVisibility(View.INVISIBLE);
                                    holder.mAsistencia.setVisibility(View.VISIBLE);
                                    holder.mAsistencia.setText("Asistencia confirmada");
                                    holder.mAsistencia.setBackgroundColor(Color.parseColor("#B6F861"));
                                }
                            }

                        }catch (JSONException e){
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
        holder.mAsistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mAsistir.setVisibility(View.INVISIBLE);
                holder.mAsistencia.setVisibility(View.VISIBLE);
                holder.mAsistencia.setText("Asistencia confirmada");
                holder.mAsistencia.setBackgroundColor(Color.parseColor("#B6F861"));
                Log.d("LISTADO ALERTA PRUEBA",current.getId_alerta());
                fetchAlert(context,Integer.valueOf(current.getId_alerta()),current.getUsername());
                Integer mas = Integer.valueOf(current.getAsistentesActuales())+1;
                holder.mAsistentesSolicitados.setText("Asistentes : "+mas+"/"+current.getAsistentesSolicitados());
            }
        });
        Log.d("EN LISTADO ALERTA",current.getNombre());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}

