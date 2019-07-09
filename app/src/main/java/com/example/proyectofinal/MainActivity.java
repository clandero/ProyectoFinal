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

public class MainActivity extends AppCompatActivity {
    TextView _username;
    TextView _role;
    Button _logout;
    String username;
    String role;

    private DrawerLayout dl;
    private ActionBarDrawerToggle adbt;
    private NavigationView view;
    private View header;

    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.historico){
                    Toast.makeText(MainActivity.this, "Registro histórico", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.notificaciones){
                    Toast.makeText(MainActivity.this, "Item 2", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.codigos){
                    Toast.makeText(MainActivity.this, "Item 3", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.instrucciones){
                    Toast.makeText(MainActivity.this, "Item 3", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.creditos){
                    Toast.makeText(MainActivity.this, "Item 3", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.logout_item){
                    //Toast.makeText(MainActivity.this, "Item 4", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.clear().commit();
                    Intent i = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(i);
                    MainActivity.this.finish();
                }
                return false;
            }
        });

        _username.setText(username);
        _role.setText(role);

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
}
