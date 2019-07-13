package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _username = findViewById(R.id.username);
        _role = findViewById(R.id.role);
        _logout = findViewById(R.id.logout);

        Intent i = getIntent();
        settings = getSharedPreferences("preferences",0);
        username = settings.getString("username", " ");
        role = settings.getString("role"," ");
        _username.setText(username);
        _role.setText(role);

        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = settings.edit();
                editor.clear().commit();
                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(i);
                MainActivity.this.finish();
            }
        });
        _tipo_alerta = findViewById(R.id.tipo_alerta);
        _nombre_alerta = findViewById(R.id.nombre_alerta);
        _direccion_alerta = findViewById(R.id.direccion_alerta);
        _detalle_alerta = findViewById(R.id.detalle_alerta);
        _asistentes_solicitados = findViewById(R.id.asistentes_solicitados);
        _asistentes_actuales = findViewById(R.id.asistentes_actuales);
        _cantidad_alertas = findViewById(R.id.cantidad_alertas);
        fetchAlert();
        Log.d("DIRECCIONNNNN",String.valueOf(fuegoCoordenadaX));
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

}
