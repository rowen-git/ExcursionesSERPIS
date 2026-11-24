package com.excursiones.model;

public class Grupo {
    
    private int idGrupo;
    private String nombreCurso;
    private String etapa;
    private int numAlumnos;

    public Grupo() {
    }

    // INSERT
    public Grupo(String nombreCurso, String etapa, int numAlumnos) {
        this.nombreCurso = nombreCurso;
        this.etapa = etapa;
        this.numAlumnos = numAlumnos;
    }

    // SELECT
    public Grupo(int idGrupo, String nombreCurso, String etapa, int numAlumnos) {
        this.idGrupo = idGrupo;
        this.nombreCurso = nombreCurso;
        this.etapa = etapa;
        this.numAlumnos = numAlumnos;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public int getNumAlumnos() {
        return numAlumnos;
    }

    public void setNumAlumnos(int numAlumnos) {
        this.numAlumnos = numAlumnos;
    }

    @Override
    public String toString() {
        return "Grupo [id Grupo=" + idGrupo + ", curso=" + nombreCurso + ", etapa=" + etapa
                + ", alumnos=" + numAlumnos + "]";
    }
}