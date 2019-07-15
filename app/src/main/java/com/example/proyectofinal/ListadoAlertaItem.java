package com.example.proyectofinal;

public class ListadoAlertaItem {
    private String nombre,tipo,detalle,direccion,asistentesSolicitados,asistentesActuales;
    public ListadoAlertaItem( String pnombre, String ptipo, String pdetalle, String pdireccion, String pasistentesSolicitados, String pasistentesActuales){
        nombre = pnombre;
        tipo = ptipo;
        detalle = pdetalle;
        direccion = pdireccion;
        asistentesSolicitados = pasistentesSolicitados;
        asistentesActuales = pasistentesActuales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getAsistentesSolicitados() {
        return asistentesSolicitados;
    }

    public void setAsistentesSolicitados(String asistentesSolicitados) {
        this.asistentesSolicitados = asistentesSolicitados;
    }

    public String getAsistentesActuales() {
        return asistentesActuales;
    }

    public void setAsistentesActuales(String asistentesActuales) {
        this.asistentesActuales = asistentesActuales;
    }
}
