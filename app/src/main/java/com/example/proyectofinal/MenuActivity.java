package com.example.proyectofinal;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MenuActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.submit);
        SharedPreferences settings = getSharedPreferences("preferences",0);
        boolean isLoggedIn = settings.getBoolean("logged_in",false);
        if(isLoggedIn){
            Intent i = new Intent(MenuActivity.this,MainActivity.class);
            startActivity(i);
            MenuActivity.this.finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Validate(username.getText().toString(), password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void Validate(final String username, String password) throws JSONException {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject x = new JSONObject();
        x.put("username",username);
        x.put("password",password);
        final JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://clandero.pythonanywhere.com/login",
                x,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response", response.toString());
                        try {
                            if(response.getInt("rsp")==1){
                                //USER HAS LOGGED IN
                                SharedPreferences settings = getSharedPreferences("preferences",0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("logged_in",true);
                                editor.putString("username",username);
                                editor.putString("role",response.getString("role"));
                                editor.commit();

                                //Cambio a ONLINE
                                JsonObjectRequest objectRequest= new JsonObjectRequest(
                                        Request.Method.PUT,
                                        "http://clandero.pythonanywhere.com/changestatus/" + username,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d("LOGOF","cambio de estado" );

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

                                //Recuperación del token de Firebase y actualización en BD
                                FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.w("FBconnection", "getInstanceId failed", task.getException());
                                                    return;
                                                }

                                                // Get new Instance ID token
                                                String token = task.getResult().getToken();

                                                // Log and toast
                                                String msg = getString(R.string.msg_token_fmt, token);
                                                Log.d("FBconnection", msg);
                                            }
                                        });

                                Intent i = new Intent(MenuActivity.this,MainActivity.class);
                                //i.putExtra("username",username);
                                //i.putExtra("role",response.getString("role"));
                                Log.d("LOGIN","Username is "+username+" and role is "+response.getString("role"));
                                startActivity(i);
                                MenuActivity.this.finish();

                            }
                            else if(response.getInt("rsp")==-1){
                                Toast.makeText(MenuActivity.this, "Nombre de usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MenuActivity.this, "Nombre de usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
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
