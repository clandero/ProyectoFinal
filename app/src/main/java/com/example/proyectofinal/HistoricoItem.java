package com.example.proyectofinal;

public class HistoricoItem {
    private String nombre;
    private String tipoalerta;
    private double coordenadaX;
    private double coordenadaY;
    private String detalle;
    public HistoricoItem(String name, String type, double posX, double posY, String detail){
        nombre = name;
        tipoalerta = type;
        coordenadaX = posX;
        coordenadaY = posY;
        detalle = detail;
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
}
