package com.example.proyectofinal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HistoricoItem {
    private String nombre;
    private String tipoalerta;
    private double coordenadaX;
    private double coordenadaY;
    private String detalle;
    private String direccion;
    private Bitmap imagen;
    public HistoricoItem(String name, String type, double posX, double posY, String detail, String dir){
        nombre = name;
        tipoalerta = type;
        coordenadaX = posX;
        coordenadaY = posY;
        detalle = detail;
        direccion = dir;
    }
    public String getNombre(){
        return nombre;
    }
    public String getTipoalerta(){
        return tipoalerta;
    }
    public String getDetalle(){
        return detalle;
    }
    public double getCoordenadaX(){
        return coordenadaX;
    }
    public double getCoordenadaY(){
        return coordenadaY;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}