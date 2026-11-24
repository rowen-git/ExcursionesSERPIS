package com.excursiones.model;

public class Profesor {
    
    private int idProfesor;
    private String nombre;
    private String apellidos;
    private int anioNacimiento;
    private String telefono;

    public Profesor() {}

    // INSERT
    public Profesor(String nombre, String apellidos, int anioNacimiento, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.anioNacimiento = anioNacimiento;
        this.telefono = telefono;
    }

    // SELECT
    public Profesor(int idProfesor, String nombre, String apellidos, int anioNacimiento, String telefono) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.anioNacimiento = anioNacimiento;
        this.telefono = telefono;
    }

   public int getIdProfesor() {
    return idProfesor;
   }

   public void setIdProfesor(int idProfesor) {
    this.idProfesor = idProfesor;
   }

   public String getNombre() {
    return nombre;
   }

   public void setNombre(String nombre) {
    this.nombre = nombre;
   }

   public String getApellidos() {
    return apellidos;
   }

   public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
   }

   public int getAnioNacimiento() {
    return anioNacimiento;
   }

   public void setAnioNacimiento(int anioNacimiento) {
    this.anioNacimiento = anioNacimiento;
   }

   public String getTelefono() {
    return telefono;
   }

   public void setTelefono(String telefono) {
    this.telefono = telefono;
   }

   @Override
   public String toString() {
    return "Profesor [id Profesor=" + idProfesor + ", nombre=" + nombre + ", apellidos=" + apellidos
            + ", Año Nacimiento=" + anioNacimiento + ", telefono=" + telefono + "]";
   }

}