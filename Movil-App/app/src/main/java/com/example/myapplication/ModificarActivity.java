package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModificarActivity extends AppCompatActivity {
    EditText textId;
    EditText textNom;
    EditText textApe;
    EditText textTel;
    EditText textEmail;
    EditText textClave;

    Button btnModificar;
    Button btnRegresar;
    Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        textId = (EditText) findViewById(R.id.txtIdMod);
        textNom = (EditText) findViewById(R.id.txtNomMod);
        textApe = (EditText) findViewById(R.id.txtApeMod);
        textTel = (EditText) findViewById(R.id.txtTelMod);
        textEmail = (EditText) findViewById(R.id.txtEmailMod);
        textClave = (EditText) findViewById(R.id.txtClaveMod);

        btnModificar = (Button) findViewById(R.id.btnModificarC);
        btnRegresar = (Button) findViewById(R.id.btnRegresarMod);
        btnBuscar = (Button) findViewById(R.id.btnBuscarMod);

        // Inicialmente deshabilitar botón modificar hasta que se busque
        btnModificar.setEnabled(false);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(v);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar(v);
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void buscar(View view) {
        String id = textId.getText().toString().trim();
        if (id.isEmpty()) {
            alertInfo("Ingrese el ID para buscar");
            return;
        }
        if (!id.matches("\\d+")) {
            alertInfo("El ID debe ser numérico");
            return;
        }

        String url = Config.URL_API + "?tipo=5&id=" + id;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.trim().isEmpty() || response.equals("null")) {
                            alertInfo("No se encontró información para ese ID");
                            return;
                        }

                        try {
                            Log.d("ModificarActivity", "Respuesta buscar: " + response);
                            JSONObject contacto = null;

                            // Manejar si devuelve Array o Object
                            if (response.trim().startsWith("[")) {
                                JSONArray jsonArray = new JSONArray(response);
                                if (jsonArray.length() > 0) {
                                    contacto = jsonArray.getJSONObject(0);
                                }
                            } else {
                                // Podría venir {"dato": null} o {"dato": []}
                                JSONObject jsonResponse = new JSONObject(response);
                                
                                if (jsonResponse.has("dato")) {
                                     if (!jsonResponse.isNull("dato")) {
                                        Object datoObj = jsonResponse.get("dato");
                                        if (datoObj instanceof JSONArray) {
                                            JSONArray arr = (JSONArray) datoObj;
                                            if(arr.length() > 0) contacto = arr.getJSONObject(0);
                                        }
                                     }
                                } else {
                                    // Asumimos que es el objeto directo
                                    contacto = jsonResponse;
                                }
                            }

                            if (contacto != null) {
                                textNom.setText(contacto.optString("nombre", contacto.optString("nom")));
                                textApe.setText(contacto.optString("apellidos", contacto.optString("app")));
                                textTel.setText(contacto.optString("telefono", contacto.optString("tel")));
                                textEmail.setText(contacto.optString("gmail", contacto.optString("email")));
                                textClave.setText(contacto.optString("clave", "0"));
                                
                                btnModificar.setEnabled(true);
                                textId.setEnabled(false); 
                                btnBuscar.setEnabled(false);
                            } else {
                                alertInfo("No se encontraron datos para el ID: " + id);
                            }

                        } catch (JSONException e) {
                            Log.e("ModificarActivity", "Error parseo: " + e.getMessage());
                            // A veces el servidor devuelve texto plano o JSON inválido si no encuentra
                            alertInfo("No se encontró el registro o error al leer: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String mensaje = "Error de conexión";
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
    }

    public void modificar(View view){
        String id = textId.getText().toString().trim();
        String nom = textNom.getText().toString().trim();
        String app = textApe.getText().toString().trim();
        String tel = textTel.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        String clave = textClave.getText().toString().trim();
        
        String error="";
        if(nom.isEmpty()){ error+="Ingrese el nombre\n"; }
        else if(!nom.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")){ error+="El nombre solo debe contener letras\n"; }

        if(app.isEmpty()){ error+="Ingrese el apellido\n"; }
        else if(!app.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")){ error+="El apellido solo debe contener letras\n"; }

        if(tel.isEmpty()){ error+="Ingrese el teléfono\n"; }
        else if(!tel.matches("\\d+")){ error+="El teléfono solo debe contener números\n"; }
        else if(tel.length() < 7 || tel.length() > 15){ error+="El teléfono debe tener entre 7 y 15 dígitos\n"; }
        
        if(email.isEmpty()){ error+="Ingrese el email\n"; }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ error+="Email inválido\n"; }

        if(clave.isEmpty()){ error+="Ingrese la clave\n"; }
        
        if(error.equals("")){
            String url = Config.URL_API + "?tipo=3&id=" + id + 
                         "&nombre=" + nom + 
                         "&apellidos=" + app + 
                         "&telefono=" + tel + 
                         "&gmail=" + email + 
                         "&clave=" + clave;
            
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                             if (response == null || response.trim().isEmpty() || response.equals("null")) {
                                alertInfo("No se pudo modificar, el registro podría no existir.");
                                return;
                             }
                             // Dependiendo de la respuesta del servidor, validar exito
                             // Asumimos éxito si llega aquí, pero idealmente parsear respuesta
                             
                            alertInfo("Se modificó el contacto correctamente");
                            
                            textId.setText("");
                            textId.setEnabled(true);
                            btnBuscar.setEnabled(true);
                            
                            textNom.setText("");
                            textApe.setText("");
                            textTel.setText("");
                            textEmail.setText("");
                            textClave.setText("");
                            btnModificar.setEnabled(false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String mensaje = "Error al modificar";
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
        } else {
            alertInfo(error);
        }
    }

    public void alertInfo(String line) {
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
