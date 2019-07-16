package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import static com.example.proyectofinal.R.id.center;
import static com.example.proyectofinal.R.id.navigation_header_role;
import static com.example.proyectofinal.R.id.navigation_header_username;

public class RegistroHistorico extends AppCompatActivity {
    TextView _username;
    TextView _role;
    String username;
    String role;
    LinearLayout list_alerts;
    TextView[] tv;
    JSONArray list_of_all_events = new JSONArray();
    private DrawerLayout dl;
    private ActionBarDrawerToggle adbt;
    private NavigationView view;
    private View header;
    SharedPreferences settings;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<HistoricoItem> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_historico);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        Intent i = getIntent();
        settings = getSharedPreferences("preferences",0);
        username = settings.getString("username", " ");
        role = settings.getString("role"," ");
        dl = findViewById(R.id.dl);
        adbt = new ActionBarDrawerToggle(this,dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(adbt);
        adbt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav_view = findViewById(R.id.nav_view);
        header = nav_view.getHeaderView(0);
        _username = header.findViewById(navigation_header_username);
        _role = header.findViewById(navigation_header_role);

        //fetchAlerts();

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.historico){
                }
                else if(id == R.id.mainactivity){
                    Intent ma = new Intent(RegistroHistorico.this, MainActivity.class);
                    startActivity(ma);
                }
                else if(id == R.id.notificaciones){
                    Intent not = new Intent(RegistroHistorico.this, Notificaciones.class);
                    startActivity(not);
                }
                else if(id == R.id.codigos){
                    Intent cod = new Intent(RegistroHistorico.this, ListadoCodigos.class);
                    startActivity(cod);
                }
                else if(id == R.id.instrucciones){
                    Intent ins = new Intent(RegistroHistorico.this, Instrucciones.class);
                    startActivity(ins);
                }
                else if(id == R.id.creditos){
                    Intent cre = new Intent(RegistroHistorico.this, Creditos.class);
                    startActivity(cre);
                }
                else if(id == R.id.logout_item){
                    //Toast.makeText(MainActivity.this, "Item 4", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.clear().commit();
                    JsonObjectRequest objectRequest= new JsonObjectRequest(
                            Request.Method.PUT,
                            "http://clandero.pythonanywhere.com/changestatus/" + username,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("LOGOF","cambio de estado" );
                                    Intent i = new Intent(RegistroHistorico.this, MenuActivity.class);
                                    startActivity(i);
                                    RegistroHistorico.this.finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Rest Response", error.toString());
                                }
                            }

                    );
                    requestQueue.add(objectRequest);

                }
                return false;
            }
        });
        _username.setText(username);
        _role.setText(role);
        list = new ArrayList<>();
        mAdapter = new HistoricoAdapter(list);
        mLayoutManager = new LinearLayoutManager(RegistroHistorico.this);
        mRecyclerView = findViewById(R.id.historicoRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        final JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://clandero.pythonanywhere.com/alert/all",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject x = response.getJSONObject(i);
                                //Log.d("ITEM",x.toString());
                                list.add(new HistoricoItem(x.getString("nombre"), x.getString("tipoalerta"), x.getDouble("coordenadaX"), x.getDouble("coordenadaY"), x.getString("detalle")));
                                //Log.d("LIST",list.get(i).getNombre());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                        //requestQueue.stop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                        //requestQueue.stop();
                    }
                });
        requestQueue.add(objectRequest);
        //list.add(new HistoricoItem("Alerta 1", "10-4", -34, -78, "GATICA SE SACO LA CHUCHA :^((("));
        //mRecyclerView = findViewById(R.id.historicoRecyclerView);
        //mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(this);
        //mAdapter = new HistoricoAdapter(list);
        //mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setAdapter(mAdapter);

    }

    public void fetchAlerts(){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://clandero.pythonanywhere.com/alert/all",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                        /*try{
                            // Loop through the array elements
                            tv = new TextView[response.length()];
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject x = response.getJSONObject(i);
                                // Get the current student (json object) data
                                //String nombre = x.getString("nombre");
                                double coordenadaX = x.getDouble("coordenadaX");
                                double coordenadaY = x.getDouble("coordenadaY");
                                int asistentesActuales = x.getInt("asistentesActuales");
                                int asistentesSolicitados = x.getInt("asistentesSolicitados");
                                String creadorAviso = x.getString("creadorAviso");
                                String detalle = x.getString("detalle");
                                String tipoAlerta = x.getString("tipoalerta");

                                Log.d("ALERT",nombre);

                                LinearLayout newItem = new LinearLayout(RegistroHistorico.this);
                                list_alerts.addView(newItem);
                                newItem.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                                newItem.setMinimumHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100,0);

                                TextView t_nombre = new TextView(RegistroHistorico.this);
                                newItem.addView(t_nombre);
                                t_nombre.setLayoutParams(params);
                                t_nombre.setGravity(center);
                                t_nombre.setText(nombre);

                                TextView t_tipoAlerta = new TextView(RegistroHistorico.this);
                                newItem.addView(t_tipoAlerta);
                                t_tipoAlerta.setLayoutParams(params);
                                t_tipoAlerta.setGravity(center);
                                t_tipoAlerta.setText(tipoAlerta);




                                // Display the formatted json data in text view
                                //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                                //mTextView.append("\n\n");
                                //TextView temp;
                                //temp = new TextView(RegistroHistorico.this);
                                //list_alerts.addView(temp);
                                //temp.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                                //temp.setGravity(center);
                                //temp.setText(nombre);

                                //tv[i] = temp;
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }*/
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.create_alert){
            Toast.makeText(RegistroHistorico.this, "CREATE ALERT", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
        else{
            return adbt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        }
    }
    //Método que guarda el estado actual de la aplicación, para ser restaurado posteriormente
    @Override
    protected void onSaveInstanceState(Bundle outState){
        //outState.putIntArray("posiciones",values);
        //outState.putInt("marcador",numero_jugadas);
        super.onSaveInstanceState(outState);
    }
    //Método que restaura el estado de la aplicación guardado previamente
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //values = savedInstanceState.getIntArray("posiciones");
        //numero_jugadas = savedInstanceState.getInt("marcador");
    }
}
