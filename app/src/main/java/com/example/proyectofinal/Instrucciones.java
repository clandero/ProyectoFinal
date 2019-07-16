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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.proyectofinal.R.id.navigation_header_role;
import static com.example.proyectofinal.R.id.navigation_header_username;

public class Instrucciones extends AppCompatActivity {
    TextView _username;
    TextView _role;
    String username;
    String role;
    private DrawerLayout dl;
    private ActionBarDrawerToggle adbt;
    private View header;
    SharedPreferences settings;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);

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
        _username.setText(username);
        _role.setText(role);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.historico){
                    Intent ri = new Intent(Instrucciones.this, RegistroHistorico.class);
                    startActivity(ri);
                }
                else if(id == R.id.mainactivity){
                    Intent ma = new Intent(Instrucciones.this, MainActivity.class);
                    startActivity(ma);
                }
                else if(id == R.id.notificaciones){
                    Intent not = new Intent(Instrucciones.this, Notificaciones.class);
                    startActivity(not);
                }
                else if(id == R.id.codigos){
                    Intent cod = new Intent(Instrucciones.this, ListadoCodigos.class);
                    startActivity(cod);
                }
                else if(id == R.id.creditos){
                    Intent cre = new Intent(Instrucciones.this, Creditos.class);
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
                                    Intent i = new Intent(Instrucciones.this, MenuActivity.class);
                                    startActivity(i);
                                    Instrucciones.this.finish();
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
        ArrayList<InstruccionesItem> list = new ArrayList<>();
        list.add(new InstruccionesItem(R.drawable.ic_place_black_24dp,"Mapa de sitaución actual","Desde aquí usted puede observar el estado actual de las emergencias en la zona, el mapa le enseñará la ubicación tanto del cuartel como de las alertas ocurridas durante las útlimas 24 horas, en las cuales usted podrá indicar si asiste o no a dicha alerta."));
        list.add(new InstruccionesItem(R.drawable.ic_history_black_24dp,"Registro histórico","Aquí puede consultar el registro histórico de alertas que han ocurrido en la zona, el tipo de alerta y el detalle de cada una, junto con su ubicación."));
        list.add(new InstruccionesItem(R.drawable.ic_notifications_active_black_24dp,"Notificaciones","Desde aquí puede ver el historial de notificaciones respecto a alertas realizadas desde comandancia y que le han llegado a su dispositivo."));
        list.add(new InstruccionesItem(R.drawable.ic_warning_black_24dp,"Listado de códigos", "En caso de que usted olvide el significado de alguno de los códigos usados por la compañía, desde aquí puede consultarlos."));
        list.add(new InstruccionesItem(R.drawable.ic_question_mark,"Instrucciones","Si tiene dudas acerca de en qué consiste alguna de las opciones en el menú, puede consultar esta pestaña para obtener detalles generales de que funciones cumplen y cómo interacturar con estas."));
        list.add(new InstruccionesItem(R.drawable.ic_contacts_black_24dp,"Créditos","Aquí se hace mención a los desarrolladores de esta aplicación."));
        list.add(new InstruccionesItem(R.drawable.ic_exit_to_app_black_24dp,"Cerrar Sesión","Si desea cerrar su sesión en esta aplicación, seleccione esta opción en el menú para salir."));
        mRecyclerView = findViewById(R.id.instruccionesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new InstruccionesAdapter(list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
            Toast.makeText(Instrucciones.this, "CREATE ALERT", Toast.LENGTH_SHORT).show();
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
