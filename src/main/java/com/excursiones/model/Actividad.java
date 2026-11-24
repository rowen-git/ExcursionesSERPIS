package com.excursiones.model;

import org.bson.types.ObjectId;

public class Actividad {

    private ObjectId idActividad;
    private String nombre;
    private int valoracion;

    public Actividad() {
    }

    public Actividad(String nombre, int valoracion) {
        this.nombre = nombre;
        this.valoracion = valoracion;
    }

    public Actividad(ObjectId idActividad, String nombre, int valoracion) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.valoracion = valoracion;
    }

    public ObjectId getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(ObjectId idActividad) {
        this.idActividad = idActividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    @Override
    public String toString() {
        return nombre + " (Valoración: " + valoracion + "/5)";
    }
}