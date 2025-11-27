package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class adapterDatos extends RecyclerView.Adapter<adapterDatos.ViewHolderDatos>{
    ArrayList<datos> lista;
    public adapterDatos(ArrayList<datos> lista) {
        this.lista = lista;
    }


    @NonNull
    @Override
    public adapterDatos.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterDatos.ViewHolderDatos holder, int position) {
        holder.asignarDatos(lista.get(position));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtId;
        TextView txtNom;
        TextView txtApp;
        TextView txtTel;
        TextView txtEmail;
        TextView txtClave;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txtId);
            txtNom = (TextView) itemView.findViewById(R.id.txtNom);
            txtApp = (TextView) itemView.findViewById(R.id.txtApp);
            txtTel = (TextView) itemView.findViewById(R.id.txtTel);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            txtClave = (TextView) itemView.findViewById(R.id.txtClave);
        }

        public void asignarDatos(datos datos) {
            txtId.setText("ID: " + datos.getId());
            txtNom.setText("Nombre: " + datos.getNom());
            txtApp.setText("Apellido: " + datos.getApp());
            txtTel.setText("Teléfono: " + datos.getTel());
            txtEmail.setText("Email: " + datos.getEmail());
            txtClave.setText("Clave: " + datos.getClave());
        }
    }
}
