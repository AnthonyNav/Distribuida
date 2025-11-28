package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class EliminarActivity extends AppCompatActivity {
    EditText textId;
    Button btnEliminar;
    Button btnRegresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_eliminar);
        textId = (EditText) findViewById(R.id.txtIdEli);
        btnEliminar = (Button) findViewById(R.id.btnEliminarC);
        btnRegresar = (Button) findViewById(R.id.btnRegresarEli);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar(v);
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void eliminar(View view){
        String id = textId.getText().toString().trim();
        String error="";
        if(id.isEmpty()){
            error+="Ingrese el ID\n";
        } else if(!id.matches("\\d+")){
            error+="El ID debe ser numérico\n";
        }
        
        if(error.equals("")){
            String url = Config.URL_API + "?tipo=4&clave=0&id=" + id;
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response == null || response.trim().isEmpty() || response.equals("null")) {
                                alertInfo("No se encontró el registro o no se pudo eliminar.");
                                return;
                            }
                            
                             if (response.trim().equalsIgnoreCase("false")) {
                                alertInfo("No se encontró el registro para eliminar.");
                                return;
                            }

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.has("error")) {
                                     alertInfo("Error: " + jsonResponse.getString("error"));
                                     return;
                                }
                            } catch (JSONException e) {
                            }

                            textId.setText("");
                            alertInfo("Se eliminó el contacto");
                        }
                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String mensaje = "Error en la petición";
                            if (error.networkResponse != null) {
                                mensaje += " (Código " + error.networkResponse.statusCode + ")";
                                if(error.networkResponse.data != null) {
                                    mensaje += "\n" + new String(error.networkResponse.data);
                                }
                            } else if (error.getMessage() != null) {
                                mensaje += ": " + error.getMessage();
                            }
                            alertInfo(mensaje);
                        }
                    });
            queue.add(stringRequest);
        }else{
            alertInfo(error);
        }
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
