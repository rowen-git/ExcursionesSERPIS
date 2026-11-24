package com.excursiones.model;

import java.time.LocalDate;

public class Excursion {
    
    private int idExcursion;
    private String destino;
    private int duracionHoras;
    private LocalDate fecha;
    private double precio;
    private boolean finalizada;
    private int idGrupo;

    public Excursion() {
    }

    // INSERT
    public Excursion(String destino, int duracionHoras, LocalDate fecha, double precio, boolean finalizada, int idGrupo) {
        this.destino = destino;
        this.duracionHoras = duracionHoras;
        this.fecha = fecha;
        this.precio = precio;
        this.finalizada = finalizada;
        this.idGrupo = idGrupo;
    }

    // SELECT
    public Excursion(int idExcursion, String destino, int duracionHoras, LocalDate fecha, double precio, boolean finalizada, int idGrupo) {
        this.idExcursion = idExcursion;
        this.destino = destino;
        this.duracionHoras = duracionHoras;
        this.fecha = fecha;
        this.precio = precio;
        this.finalizada = finalizada;
        this.idGrupo = idGrupo;
    }

    public int getIdExcursion() {
        return idExcursion;
    }

    public void setIdExcursion(int idExcursion) {
        this.idExcursion = idExcursion;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(int duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    @Override
    public String toString() {
        return "Excursión [id=" + idExcursion + ", destino=" + destino + ", duración=" + duracionHoras 
                + "h, fecha=" + fecha + ", precio=" + precio + "€, finalizada=" + finalizada 
                + ", idGrupo=" + idGrupo + "]";
    }
}