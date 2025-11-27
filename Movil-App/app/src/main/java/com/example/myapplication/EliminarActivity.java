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
        String id = textId.getText().toString();
        String error="";
        if(id.equals("")){
            error+="Ingrese el ID\n";
        }
        if(error.equals("")){
            String url = Config.URL_API + "?tipo=4&clave=0&id=" + id + "&r="+new Random().nextInt();
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            textId.setText("");
                            alertInfo("Se elimino el contacto");
                        }
                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            alertInfo(error.getMessage().toString());
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