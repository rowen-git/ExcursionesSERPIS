package com.excursiones.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

public class Resena {

    private ObjectId id;
    private int idExcursion;
    private LocalDateTime fechaCreacion;
    private int valoracionGlobal;
    private String comentarioGeneral;
    private List<Actividad> actividades = new ArrayList<>();
    private List<String> incidencias = new ArrayList<>();

    public Resena() {
    }

    public Resena(int idExcursion, int valoracionGlobal, String comentarioGeneral,
                  List<Actividad> actividades, List<String> incidencias) {
        this.idExcursion = idExcursion;
        this.valoracionGlobal = valoracionGlobal;
        this.comentarioGeneral = comentarioGeneral;
        this.actividades = (actividades != null) ? actividades : new ArrayList<>();
        this.incidencias = (incidencias != null) ? incidencias : new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getIdExcursion() {
        return idExcursion;
    }

    public void setIdExcursion(int idExcursion) {
        this.idExcursion = idExcursion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getValoracionGlobal() {
        return valoracionGlobal;
    }

    public void setValoracionGlobal(int valoracionGlobal) {
        this.valoracionGlobal = valoracionGlobal;
    }

    public String getComentarioGeneral() {
        return comentarioGeneral;
    }

    public void setComentarioGeneral(String comentarioGeneral) {
        this.comentarioGeneral = comentarioGeneral;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = (actividades != null) ? actividades : new ArrayList<>();
    }

    public List<String> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(List<String> incidencias) {
        this.incidencias = (incidencias != null) ? incidencias : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Reseña [idExcursion=" + idExcursion + ", fechaCreacion=" + fechaCreacion
                + ", valoracionGlobal=" + valoracionGlobal + "/5, comentarioGeneral=" + comentarioGeneral
                + ", actividades=" + actividades + ", incidencias=" + incidencias + "]";
    }
}