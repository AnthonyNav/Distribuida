package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MostrarActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

        btnRegresar = (Button) findViewById(R.id.btnRegresarMostrar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mostrar();
    }

    public void mostrar(){
        String url = Config.URL_API + "?tipo=1&r=" + new Random().nextInt();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            alertInfo("La respuesta del servidor es nula");
                            return;
                        }
                        
                        response = response.trim();
                        
                        if (response.isEmpty()) {
                            alertInfo("La respuesta del servidor está vacía");
                            return;
                        }

                        JSONArray jsonArray = null;

                        try{
                            System.out.println("--------------------");
                            System.out.println("RESPUESTA SERVIDOR: " + response);
                            System.out.println("--------------------");

                            if (response.startsWith("[")) {
                                jsonArray = new JSONArray(response);
                            } else if (response.startsWith("{")) {
                                JSONObject jsonResponse = new JSONObject(response);
                                jsonArray = jsonResponse.optJSONArray("dato");
                                if (jsonArray == null) jsonArray = jsonResponse.optJSONArray("lista");
                                if (jsonArray == null) jsonArray = jsonResponse.optJSONArray("data");
                                if (jsonArray == null) jsonArray = jsonResponse.optJSONArray("contactos");
                                
                                if (jsonArray == null) {
                                    alertInfo("El JSON recibido es válido pero no contiene una lista de datos conocida.");
                                    Log.d("MostrarActivity", "JSON Keys: " + jsonResponse.names());
                                }
                            } else {
                                String preview = response.length() > 200 ? response.substring(0, 200) + "..." : response;
                                alertInfo("El servidor no devolvió JSON válido:\n" + preview);
                                return;
                            }

                        } catch (JSONException e){
                            Log.e("MostrarActivity", "Error al parsear JSON: " + e.getMessage());
                            alertInfo("Error de formato JSON: " + e.getMessage());
                            return;
                        }

                        ArrayList<datos> listaDatos = new ArrayList<>();

                        if(jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject objecto = jsonArray.getJSONObject(i);
                                    
                                    String nom = objecto.optString("nombre", objecto.optString("nom", "Sin nombre"));
                                    String app = objecto.optString("apellidos", objecto.optString("app", "Sin apellido"));
                                    String tel = objecto.optString("telefono", objecto.optString("tel", "Sin telefono"));
                                    String email = objecto.optString("gmail", objecto.optString("email", "Sin email"));
                                    String clave = objecto.optString("clave", "");
                                    
                                    int id = 0;
                                    if (objecto.has("id")) {
                                        id = objecto.optInt("id");
                                        if (id == 0) {
                                            try {
                                                id = Integer.parseInt(objecto.getString("id"));
                                            } catch (Exception ex) { id = 0; }
                                        }
                                    }
                                    
                                    listaDatos.add(new datos(id, nom, app, tel, email, clave));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapterDatos adapter = new adapterDatos(listaDatos);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String mensaje = "Error de conexión";
                        if (error.networkResponse != null) {
                            mensaje += " (Código " + error.networkResponse.statusCode + ")";
                        }
                        alertInfo(mensaje + ": " + (error.getMessage() != null ? error.getMessage() : ""));
                    }
                });
        queue.add(stringRequest);
    }
    public void alertInfo(String line){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información")
                .setMessage(line)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }
}
