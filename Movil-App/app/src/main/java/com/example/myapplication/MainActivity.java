package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnCrear;
    Button btnMostrar;
    Button btnEliminar;
    Button btnModificar;
    ImageButton btnTema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCrear = (Button) findViewById(R.id.button1);
        btnModificar = (Button) findViewById(R.id.button2);
        btnEliminar = (Button) findViewById(R.id.button3);
        btnMostrar = (Button) findViewById(R.id.button4);
        btnTema = (ImageButton) findViewById(R.id.btnTema);

        // Configurar icono inicial
        actualizarIconoTema();

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CrearActivity.class);
                startActivity(i);
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MostrarActivity.class);
                startActivity(i);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EliminarActivity.class);
                startActivity(i);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ModificarActivity.class);
                startActivity(i);
            }
        });

        btnTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentMode = AppCompatDelegate.getDefaultNightMode();
                if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                // El recrear la actividad actualizará el icono en onCreate, 
                // pero por si acaso también lo llamamos aquí aunque la app se recreará
                actualizarIconoTema();
            }
        });
    }

    private void actualizarIconoTema() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            // Estamos en oscuro, mostrar SOL para cambiar a claro
            btnTema.setImageResource(R.drawable.ic_sun);
        } else {
            // Estamos en claro (o automático), mostrar LUNA para cambiar a oscuro
            btnTema.setImageResource(R.drawable.ic_moon);
        }
    }
}
