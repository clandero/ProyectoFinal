package com.example.proyectofinal;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static com.example.proyectofinal.R.id.navigation_header_role;
import static com.example.proyectofinal.R.id.navigation_header_username;
import static com.example.proyectofinal.R.id.start;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private NetworkChangeReceiver netchange = new NetworkChangeReceiver();

    //Username y Role del usuario en la barra de navegación
    TextView _username;
    TextView _role;
    String username;
    String role;
    //Variables para el manejo de la barra de navegación
    private DrawerLayout dl;
    private ActionBarDrawerToggle adbt;
    private NavigationView view;
    private View header;
    private Button token;
    SharedPreferences settings;
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
        NavigationView nav_view = findViewById(R.id.nav_view);
        header = nav_view.getHeaderView(0);
        _username = header.findViewById(navigation_header_username);
        _role = header.findViewById(navigation_header_role);
        _username.setText(username);
        _role.setText(role);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.historico){
                    Intent ri = new Intent(MainActivity.this, RegistroHistorico.class);
                    startActivity(ri);
                }
                else if(id == R.id.notificaciones){
                    Intent not = new Intent(MainActivity.this, Notificaciones.class);
                    startActivity(not);
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
