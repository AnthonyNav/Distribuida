package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CrearActivity extends AppCompatActivity {
    EditText textNom;
    EditText textApe;
    EditText textTel;
    EditText textEmail;
    EditText textClave;

    Button btnCrear;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);
        textNom = (EditText) findViewById(R.id.txtNom);
        textApe = (EditText) findViewById(R.id.txtApe);
        textTel = (EditText) findViewById(R.id.txtTel);
        textEmail = (EditText) findViewById(R.id.txtEmail);
        textClave = (EditText) findViewById(R.id.txtClave);

        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnRegresar = (Button) findViewById(R.id.btnRegresar);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar(v);
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void enviar(View view){
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
            String url = Config.URL_API + "?tipo=2&nombre=" + nom + 
                         "&apellidos=" + app + 
                         "&telefono=" + tel + 
                         "&gmail=" + email + 
                         "&clave=" + clave;
            
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            textNom.setText("");
                            textApe.setText("");
                            textTel.setText("");
                            textEmail.setText("");
                            textClave.setText("");
                            alertInfo("Se creó el contacto");
                        }
                    },
                    new Response.ErrorListener() {
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
