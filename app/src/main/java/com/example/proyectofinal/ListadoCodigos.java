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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.proyectofinal.R.id.navigation_header_role;
import static com.example.proyectofinal.R.id.navigation_header_username;

public class ListadoCodigos extends AppCompatActivity {
    TextView _username;
    TextView _role;
    String username;
    String role;
    private DrawerLayout dl;
    private ActionBarDrawerToggle adbt;
    private NavigationView view;
    private View header;
    SharedPreferences settings;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_codigos);
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
                    Intent ri = new Intent(ListadoCodigos.this, RegistroHistorico.class);
                    startActivity(ri);
                }
                else if(id == R.id.mainactivity){
                    Intent ma = new Intent(ListadoCodigos.this, MainActivity.class);
                    startActivity(ma);
                }

                else if(id == R.id.instrucciones){
                    Intent ins = new Intent(ListadoCodigos.this, Instrucciones.class);
                    startActivity(ins);
                }
                else if(id == R.id.creditos){
                    Intent cre = new Intent(ListadoCodigos.this, Creditos.class);
                    startActivity(cre);
                }
                else if(id == R.id.logout_item){
                    //Toast.makeText(MainActivity.this, "Item 4", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.clear().commit();
                    Intent i = new Intent(ListadoCodigos.this, MenuActivity.class);
                    startActivity(i);
                    ListadoCodigos.this.finish();
                }
                return false;
            }
        });

        ArrayList<ListadoCodigoItem> list = new ArrayList<>();
        list.add(new ListadoCodigoItem(R.drawable.ic_incendio,"10-0    Fuego o humo en construcción"));
        list.add(new ListadoCodigoItem(R.drawable.ic_car_on_fire,"10-1    Fuego en vehículo"));
        list.add(new ListadoCodigoItem(R.drawable.ic_bonfire,"10-2    Fuego en pastizales o basura"));
        list.add(new ListadoCodigoItem(R.drawable.ic_emergency,"10-3    Emergencia"));
        list.add(new ListadoCodigoItem(R.drawable.ic_vehiche,"10-4    Rescate Vehicular"));
        list.add(new ListadoCodigoItem(R.drawable.ic_biohazard,"10-5    Materiales peligrosos"));
        list.add(new ListadoCodigoItem(R.drawable.ic_gas_mask,"10-6    Emanación de gases"));
        list.add(new ListadoCodigoItem(R.drawable.ic_electrical,"10-7    Accidente eléctrico"));
        list.add(new ListadoCodigoItem(R.drawable.ic_question_mark,"10-8    Llamado no clasificado"));
        list.add(new ListadoCodigoItem(R.drawable.ic_warning_black_24dp,"10-9    Otros servicios"));
        list.add(new ListadoCodigoItem(R.drawable.ic_falling_rocks,"10-10    Rebrote de escombros"));
        list.add(new ListadoCodigoItem(R.drawable.ic_plane,"10-11    Servicio aéreo"));
        list.add(new ListadoCodigoItem(R.drawable.ic_support,"10-12    Apoyo a otros cuerpos"));
        list.add(new ListadoCodigoItem(R.drawable.ic_explosion,"10-13    Artefacto explosivo"));
        list.add(new ListadoCodigoItem(R.drawable.ic_plane_crash,"10-14    Accidente aéreo"));
        list.add(new ListadoCodigoItem(R.drawable.ic_simulacro,"10-15    Simulacro"));

        mRecyclerView = findViewById(R.id.listadoCodigosRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ListadoCodigoAdapter(list);
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
            Toast.makeText(ListadoCodigos.this, "CREATE ALERT", Toast.LENGTH_SHORT).show();
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
