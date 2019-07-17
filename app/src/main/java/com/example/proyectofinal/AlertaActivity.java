package com.example.proyectofinal;

import android.app.Activity;
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
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.proyectofinal.R.id.company;
import static com.example.proyectofinal.R.id.descripcion;
import static com.example.proyectofinal.R.id.navigation_header_role;
import static com.example.proyectofinal.R.id.navigation_header_username;
import static com.example.proyectofinal.R.id.start;

public class AlertaActivity extends AppCompatActivity {

    private static final String TAG = "AlertaActivity";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AIzaSyAyfYyZJiYThGxmKu5jNUW3x10Eye-iJqQ";
    final private String contentType = "application/json";

    //vaiables de la activity
    private EditText nombre,tipo,descr,coordx,coordy,asist,compa;
    private Button send;

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
    private String token;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerta);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FBconnection", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("FBconnection", msg);
                    }
                });

        //Obtiene de SharedPreferences los datos del usuario que inicio sesión
        Intent i = getIntent();
        settings = getSharedPreferences("preferences",0);
        username = settings.getString("username", " ");
        role = settings.getString("role"," ");

        //captura elementos
        nombre = findViewById(R.id.nombre_al);
        tipo = findViewById(R.id.tipo);
        descr = findViewById(R.id.descripcion);
        coordx = findViewById(R.id.coordx);
        coordy = findViewById(R.id.coordy);
        compa = findViewById(R.id.company);
        asist = findViewById(R.id.asist);
        send = findViewById(R.id.publicar);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nombre.getText().toString();
                String type = tipo.getText().toString();
                String des = descr.getText().toString();
                String xcor = coordx.getText().toString();
                float x = Float.parseFloat(xcor);
                String ycor = coordy.getText().toString();
                float y = Float.parseFloat(ycor);
                String comp = compa.getText().toString();
                int co = Integer.parseInt(comp);
                String assist = asist.getText().toString();
                int as = Integer.parseInt(assist);
                generarAlerta(name,type,des,x,y,co,as,requestQueue);
            }
        });
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
                    Intent ri = new Intent(AlertaActivity.this, RegistroHistorico.class);
                    startActivity(ri);
                }
                else if(id == R.id.codigos){
                    Intent cod = new Intent(AlertaActivity.this, ListadoCodigos.class);
                    startActivity(cod);
                }
                else if(id == R.id.instrucciones){
                    Intent ins = new Intent(AlertaActivity.this, Instrucciones.class);
                    startActivity(ins);
                }
                else if(id == R.id.creditos){
                    Intent cre = new Intent(AlertaActivity.this, Creditos.class);
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
                                    Intent i = new Intent(AlertaActivity.this, MenuActivity.class);
                                    startActivity(i);
                                    AlertaActivity.this.finish();
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
    private void generarAlerta(String name, final String type, final String desc, float xcor, float ycor, final int comp, int assist, final RequestQueue req){
        JSONObject x1 = new JSONObject();
        try {
            x1.put("nombre",name);
            x1.put("tipoalerta",type);
            x1.put("detalle",desc);
            x1.put("creadorAviso",username);
            x1.put("coordenadaX",xcor);
            x1.put("coordenadaY",ycor);
            x1.put("asistentesSolicitados",assist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ArrayList<UserItem> list = new ArrayList<>();

        final JsonObjectRequest firebase_request= new JsonObjectRequest(
                Request.Method.POST,
                "http://clandero.pythonanywhere.com/alert",
                x1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ALAARM","DONE" );
                        //creación de notificaciones

                        final JsonArrayRequest objectRequest = new JsonArrayRequest(
                                Request.Method.GET,
                                "http://clandero.pythonanywhere.com/users/all",
                                null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        Log.e("Rest RETRIEVE Response", response.toString());
                                        Log.d("SIZE RESP","" + response.length());
                                        for(int i=0;i<response.length();i++){
                                            try {
                                                JSONObject x = response.getJSONObject(i);
                                                //Log.d("ITEM",x.toString());
                                                list.add(new UserItem(x.getString("username"), x.getString("firebase_token"), x.getString("phone"), x.getString("status"), x.getInt("company")));
                                                //Log.d("LIST",list.get(i).getNombre());

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        sendNotifications(list,comp,type,desc);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("RestRETRIEVErrResponse", error.toString());
                                        //requestQueue.stop();
                                    }
                                });
                        req.add(objectRequest);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest ALARM Response", error.toString());
                    }
                }

        );
        req.add(firebase_request);
    }

    public void sendNotifications(ArrayList<UserItem> users, int company,String alerta, String descr){

        ArrayList<String> phones = new ArrayList<>();
        ArrayList<String> fb_tokens = new ArrayList<>();
        for (int i = 0; i< users.size();i++){
            Log.d("CoMPARE", " " + company + " con " + users.get(i).getCompany());
            if (users.get(i).getCompany() == company){
                Log.d("CoMPARE2", " online con " + users.get(i).getStatus());
                if(users.get(i).getStatus().equals("online")){
                    fb_tokens.add(users.get(i).getFb_token());
                }
                else {
                    phones.add(users.get(i).getPhone());
                }
            }
        }
        Log.d("PHONE","" + phones.size());
        Log.d("FBTOKE","" + fb_tokens.size());

        if (phones.size() > 0){
            for(int i = 0;i<phones.size();i++) {
                try{
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(phones.get(i),null,alerta + " " + descr,null,null);
                    Toast.makeText(AlertaActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(AlertaActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (fb_tokens.size() > 0){

            String result = TextUtils.join(",",fb_tokens);

            Log.d("LOG FBTOK","" + result);

            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();

            try {
                notifcationBody.put("title", alerta);
                notifcationBody.put("body", descr);

                notification.put("to",result );
                notification.put("notification", notifcationBody);

            } catch (JSONException e) {
                Log.e(TAG, "onCreate: " + e.getMessage() );
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "onResponse: " + response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AlertaActivity.this, "Request error", Toast.LENGTH_LONG).show();
                            Log.i(TAG, "onErrorResponse: Didn't work");
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", serverKey);
                    params.put("Content-Type", contentType);
                    return params;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        }



        Log.d("LOG REDY","RDYDYDYDYD" );

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        return adbt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

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
        super.onDestroy();
    }
}
