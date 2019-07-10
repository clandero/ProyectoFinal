package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView _username;
    TextView _role;
    Button _logout;
    String username;
    String role;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _username = findViewById(R.id.username);
        _role = findViewById(R.id.role);
        _logout = findViewById(R.id.logout);
        addFragment(new MapaFragment(), false, "one");

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
}
