package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.proyectofinal.R.id.navigation_header_role;
import static com.example.proyectofinal.R.id.navigation_header_username;
import static com.example.proyectofinal.R.id.start;

public class MainActivity extends AppCompatActivity {
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
    SharedPreferences settings;
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
