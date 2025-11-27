package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
        String id = textId.getText().toString();
        if (id.equals("")) {
            alertInfo("Ingrese el ID para buscar");
            return;
        }

        String url = Config.URL_API + "?tipo=5&id=" + id + "&r=" + new Random().nextInt();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.trim().isEmpty()) {
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
                                JSONObject jsonResponse = new JSONObject(response);
                                // A veces viene directo o dentro de "dato"
                                if (jsonResponse.has("dato")) {
                                     JSONArray arr = jsonResponse.optJSONArray("dato");
                                     if(arr != null && arr.length() > 0) contacto = arr.getJSONObject(0);
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
                                
                                // La clave por lo general no se devuelve por seguridad, pero si viene, la ponemos.
                                // Si no, ponemos "0" o vacío.
                                textClave.setText(contacto.optString("clave", "0"));
                                
                                btnModificar.setEnabled(true);
                                // Bloquear ID para no cambiarlo accidentalmente
                                textId.setEnabled(false); 
                                btnBuscar.setEnabled(false);
                            } else {
                                alertInfo("No se encontraron datos para el ID: " + id);
                            }

                        } catch (JSONException e) {
                            Log.e("ModificarActivity", "Error parseo: " + e.getMessage());
                            alertInfo("Error al leer los datos: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alertInfo("Error de conexión: " + error.getMessage());
                    }
                });
        queue.add(stringRequest);
    }

    public void modificar(View view){
        String id = textId.getText().toString();
        String nom = textNom.getText().toString();
        String app = textApe.getText().toString();
        String tel = textTel.getText().toString();
        String email = textEmail.getText().toString();
        String clave = textClave.getText().toString();
        
        String error="";
        if(nom.equals("")){ error+="Ingrese el nombre\n"; }
        if(app.equals("")){ error+="Ingrese el apellido\n"; }
        if(tel.equals("")){ error+="Ingrese el teléfono\n"; }
        
        if(clave.equals("")) clave = "0";
        
        if(error.equals("")){
            String url = Config.URL_API + "?tipo=3&id=" + id + 
                         "&nombre=" + nom + 
                         "&apellidos=" + app + 
                         "&telefono=" + tel + 
                         "&gmail=" + email + 
                         "&clave=" + clave + 
                         "&r=" + new Random().nextInt();
            
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            alertInfo("Se modificó el contacto correctamente");
                            // Resetear formulario para otra búsqueda
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
                            alertInfo(error.getMessage() != null ? error.getMessage() : "Error al modificar");
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
