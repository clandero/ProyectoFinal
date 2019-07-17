package com.example.proyectofinal;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import static com.example.proyectofinal.R.id.navigation_header_role;
import static com.example.proyectofinal.R.id.navigation_header_username;
import static com.example.proyectofinal.R.id.start;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private NetworkChangeReceiver netchange = new NetworkChangeReceiver();

    //Username y Role del usuario en la barra de navegación
    TextView _cantidad_alertas;
    TextView[] tv;
    Button _logout;
    String username;
    String role;
    String direccion_alerta;
    Vector<String> nombre_alerta;
    Vector<Double> alertaCoordenadaX;
    Vector<Double> alertaCoordenadaY;
    Vector<String> tipo_alerta;
    String detalle_alerta;
    Integer asistentes_solicitados;
    Integer asistentes_actuales;
    Integer cantidad_alertas;
    Integer id_alerta;
    String fecha_alerta;
    ArrayList<ListadoAlertaItem> list;
    MapaFragment mf;
    SharedPreferences settings;
    //Variables para el manejo de la barra de navegación
    private DrawerLayout dl;
    private ActionBarDrawerToggle adbt;
    private NavigationView view;
    private View header;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Chequea conexión con datos
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        netchange = new NetworkChangeReceiver();
        this.registerReceiver(netchange, filter);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Obtiene de SharedPreferences los datos del usuario que inicio sesión
        Intent i = getIntent();
        settings = getSharedPreferences("preferences",0);
        username = settings.getString("username", " ");
        role = settings.getString("role"," ");
        //Barra de navegación
        dl = findViewById(R.id.dl);
        adbt = new ActionBarDrawerToggle(this,dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(adbt);
        adbt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Barra de navegación
        _username = header.findViewById(navigation_header_username);
        _role = header.findViewById(navigation_header_role);
        _username.setText(username);
        _role.setText(role);
        NavigationView nav_view = findViewById(R.id.nav_view);
        header = nav_view.getHeaderView(0);
        _cantidad_alertas = findViewById(R.id.cantidad_alertas);
        nombre_alerta = new Vector<String>();
        alertaCoordenadaX = new Vector<Double>();
        alertaCoordenadaY = new Vector<Double>();
        tipo_alerta = new Vector<String>();
        list = new ArrayList<>();
        fetchAlert();
        mRecyclerView = findViewById(R.id.listadoAlertasRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ListadoAlertaAdapter(list,this);
        Log.d("DIRECCION EN MAIN",String.valueOf(list.size()));
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.historico){
                    Intent ri = new Intent(MainActivity.this, RegistroHistorico.class);
                    startActivity(ri);
                }

                else if(id == R.id.codigos){
                    Intent cod = new Intent(MainActivity.this, ListadoCodigos.class);
                    startActivity(cod);
                }
                else if(id == R.id.instrucciones){
                    Intent ins = new Intent(MainActivity.this, Instrucciones.class);
                    startActivity(ins);
                }
                else if(id == R.id.creditos){
                    Intent cre = new Intent(MainActivity.this, Creditos.class);
                    startActivity(cre);
                }
                //Opción para Log Out
                else if(id == R.id.logout_item){
                    SharedPreferences.Editor editor = settings.edit();
                    editor.clear().commit();

                    //Cambio a OFFLINE del usuario
                    JsonObjectRequest objectRequest= new JsonObjectRequest(
                            Request.Method.PUT,
                            "http://clandero.pythonanywhere.com/changestatus/" + username,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("LOGOF","cambio de estado" );
                                    Intent i = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(i);
                                    MainActivity.this.finish();
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
    }
    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.fragment_vista, fragment, tag);
        ft.commitAllowingStateLoss();
    }
    public void fetchAlert(){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://clandero.pythonanywhere.com/alert/last24hours",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                        try {
                            for( int i = response.length()-1; i >= 0 ; i--){
                                // Loop through the array elements
                                tv = new TextView[response.length()];
                                // Get current json object
                                JSONObject x = response.getJSONObject(i);
                                // Get the current student (json object) data
                                tipo_alerta.add(x.getString("tipoalerta"));
                                nombre_alerta.add(x.getString("nombre"));
                                alertaCoordenadaX.add(x.getDouble("coordenadaX"));
                                Log.d("DIRECCIONNNNN2", String.valueOf(alertaCoordenadaX.lastElement()));
                                alertaCoordenadaY.add(x.getDouble("coordenadaY"));
                                try {
                                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(alertaCoordenadaX.lastElement(), alertaCoordenadaY.lastElement(), 1);
                                    if (addresses.size() > 0) {
                                        Address address = addresses.get(0);
                                        direccion_alerta = address.getAddressLine(0);
                                        Log.d("DIRECCIONCOMPLETA", direccion_alerta);
                                    }


                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                                detalle_alerta = x.getString("detalle");
                                asistentes_solicitados = x.getInt("asistentesSolicitados");
                                asistentes_actuales = x.getInt("asistentesActuales");
                                cantidad_alertas = response.length();
                                fecha_alerta = x.getString("fecha");
                                id_alerta = x.getInt("id");
                                _cantidad_alertas.setText("Alertas últimas 24 horas: "+String.valueOf(cantidad_alertas));
                                list.add(new ListadoAlertaItem(nombre_alerta.lastElement(),tipo_alerta.lastElement(),detalle_alerta,direccion_alerta,String.valueOf(asistentes_solicitados),String.valueOf(asistentes_actuales),fecha_alerta,String.valueOf(id_alerta),username));
                                Log.d("DIRECCION EN RESPONSE",String.valueOf(list.size()));
                            }
                            if (response.length() == 0){
                                _cantidad_alertas.setText("No existen alertas en las últimas 24 horas.");
                            }
                            mf = new MapaFragment(alertaCoordenadaX,alertaCoordenadaY,nombre_alerta,tipo_alerta);
                            addFragment(mf, false, "one");
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);

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
            Toast.makeText(MainActivity.this, "CREATE ALERT", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this,AlertaActivity.class);
            startActivity(i);
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
    @Override
    protected void onDestroy() {
        Log.d("baja","offline a la BD");
        super.onDestroy();
        if (netchange != null) {
            this.unregisterReceiver(netchange);
        }


    }
}
