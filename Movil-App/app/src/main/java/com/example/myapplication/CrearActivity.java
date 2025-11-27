package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
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
        String nom = textNom.getText().toString();
        String app = textApe.getText().toString();
        String tel = textTel.getText().toString();
        String email = textEmail.getText().toString();
        String clave = textClave.getText().toString();
        
        String error="";
        if(nom.equals("")){ error+="Ingrese el nombre\n"; }
        if(app.equals("")){ error+="Ingrese el apellido\n"; }
        if(tel.equals("")){ error+="Ingrese el teléfono\n"; }
        // Email y clave opcionales según lógica previa pero mejor validar si se requiere
        
        if(clave.equals("")) clave = "0"; // Valor por defecto si está vacío

        if(error.equals("")){
            String url = Config.URL_API + "?tipo=2&nombre=" + nom + 
                         "&apellidos=" + app + 
                         "&telefono=" + tel + 
                         "&gmail=" + email + 
                         "&clave=" + clave + 
                         "&r=3423432423";
            
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            textNom.setText("");
                            textApe.setText("");
                            textTel.setText("");
                            textEmail.setText("");
                            textClave.setText("0");
                            alertInfo("Se creó el contacto");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            alertInfo(error.getMessage() != null ? error.getMessage() : "Error en la petición");
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
