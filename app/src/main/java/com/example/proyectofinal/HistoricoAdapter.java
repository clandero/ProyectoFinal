package com.example.proyectofinal;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {
    private ArrayList<HistoricoItem> mList;
    private AppCompatActivity mActivity;
    Bitmap m;
    public static class HistoricoViewHolder extends RecyclerView.ViewHolder{
        //LO QUE SEA QUE ES EL ELEMENTO PARA EL MAPA :^)
        public TextView mTitle;
        public TextView mType;
        public TextView mContent;
        public ImageView mImagen;
        public HistoricoViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.historico_nombre_alerta);
            mType = itemView.findViewById(R.id.historico_tipo_alerta);
            mContent = itemView.findViewById(R.id.historico_detalle);
            mImagen = itemView.findViewById(R.id.mapa_imagen);
        }
    }

    public HistoricoAdapter(ArrayList<HistoricoItem> list, AppCompatActivity activity){
        mList = list;
        mActivity =activity;
    }

    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historico_item, parent,false);
        HistoricoViewHolder lcvh = new HistoricoViewHolder(v);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(HistoricoViewHolder holder, int i) {
        final HistoricoItem current = mList.get(i);
        holder.mTitle.setText(current.getNombre());
        holder.mType.setText(current.getTipoalerta());
        holder.mContent.setText(current.getDetalle()+"\n"+current.getDireccion());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=18&size=560x240&markers=size:mid|color:red|"
                            + current.getCoordenadaX()
                            + ","
                            + current.getCoordenadaY()
                            + "&key=AIzaSyAkCft9O8BfWMAxtLIZZ6lS819mn1JZ5tc"
                            + "&sensor=false";
                    URL aurl = new URL(getMapURL);
                    HttpURLConnection connection = (HttpURLConnection) aurl.openConnection();
                    InputStream input = connection.getInputStream();
                    Log.d("DIRECCION ADAPTER 1 ",String.valueOf(m));
                    m = BitmapFactory.decodeStream(input);
                    Log.d("DIRECCION ADAPTER 2 ",String.valueOf(m));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        while(m==null){

        }
        current.setImagen(m);
        holder.mImagen.setImageBitmap(current.getImagen());
        Log.d("DIRECCION ADAPTER 3 ",String.valueOf(m));
        m = null;


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}