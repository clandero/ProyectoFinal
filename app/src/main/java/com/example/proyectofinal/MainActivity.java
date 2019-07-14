package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.List;
import java.util.Locale;

import static com.example.proyectofinal.R.id.navigation_header_role;
import static com.example.proyectofinal.R.id.navigation_header_username;
import static com.example.proyectofinal.R.id.start;

public class MainActivity extends AppCompatActivity {
    //Username y Role del usuario en la barra de navegación
    TextView _username;
    TextView _role;
    TextView _tipo_alerta;
    TextView _nombre_alerta;
    TextView _direccion_alerta;
    TextView _detalle_alerta;
    TextView _asistentes_solicitados;
    TextView _asistentes_actuales;
    TextView _cantidad_alertas;
    TextView[] tv;
    Button _logout;
    String username;
    String role;
    String tipo_alerta;
    String nombre_alerta;
    String direccion_alerta;
    Double fuegoCoordenadaX;
    Double fuegoCoordenadaY;
    String detalle_alerta;
    Integer asistentes_solicitados;
    Integer asistentes_actuales;
    Integer cantidad_alertas;
    SharedPreferences settings;
    //Variables para el manejo de la barra de navegación
    private DrawerLayout dl;
    private ActionBarDrawerToggle adbt;
    private NavigationView view;
    private View header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        _tipo_alerta = findViewById(R.id.tipo_alerta);
        _nombre_alerta = findViewById(R.id.nombre_alerta);
        _direccion_alerta = findViewById(R.id.direccion_alerta);
        _detalle_alerta = findViewById(R.id.detalle_alerta);
        _asistentes_solicitados = findViewById(R.id.asistentes_solicitados);
        _asistentes_actuales = findViewById(R.id.asistentes_actuales);
        _cantidad_alertas = findViewById(R.id.cantidad_alertas);
        fetchAlert();
        Log.d("DIRECCIONNNNN",String.valueOf(fuegoCoordenadaX));
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
                    Intent i = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(i);
                    MainActivity.this.finish();
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
                        try{
                            //for(int i=0;i<response.length();i++){
                            // Loop through the array elements
                            tv = new TextView[response.length()];
                            // Get current json object
                            JSONObject x = response.getJSONObject(response.length()-1);
                            // Get the current student (json object) data
                            tipo_alerta = x.getString("tipoalerta");
                            nombre_alerta = x.getString("nombre");
                            fuegoCoordenadaX = x.getDouble("coordenadaX");
                            Log.d("DIRECCIONNNNN2",String.valueOf(fuegoCoordenadaX));
                            fuegoCoordenadaY = x.getDouble("coordenadaY");
                            try{
                                Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(fuegoCoordenadaX,fuegoCoordenadaY,1);
                                if(addresses.size()>0){
                                    Address address = addresses.get(0);
                                    direccion_alerta = address.getAddressLine(0);
                                    Log.d("DIRECCIONCOMPLETA",direccion_alerta);
                                }


                            }catch(IOException ex){
                                ex.printStackTrace();
                            }

                            detalle_alerta = x.getString("detalle");
                            asistentes_solicitados = x.getInt("asistentesSolicitados");
                            asistentes_actuales = x.getInt("asistentesActuales");
                            cantidad_alertas = response.length();
                            _tipo_alerta.setText(tipo_alerta);
                            _nombre_alerta.setText(nombre_alerta);
                            _detalle_alerta.setText(detalle_alerta);
                            _direccion_alerta.setText("Dirección: "+String.valueOf(direccion_alerta));
                            _asistentes_solicitados.setText("Asistentes solicitados: "+String.valueOf(asistentes_solicitados));
                            _asistentes_actuales.setText("Asistentes actuales: "+String.valueOf(asistentes_actuales));
                            _cantidad_alertas.setText("Cantidad de alertas diarias: "+String.valueOf(cantidad_alertas));
                            addFragment(new MapaFragment(fuegoCoordenadaX,fuegoCoordenadaY), false, "one");
                            //}
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
