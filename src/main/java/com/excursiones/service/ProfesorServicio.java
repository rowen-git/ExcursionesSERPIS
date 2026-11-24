package com.excursiones.service;

import com.excursiones.dao.ProfesorDao;
import com.excursiones.model.Profesor;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ProfesorServicio {

    private ProfesorDao profesorDao;

    public ProfesorServicio() {
        this.profesorDao = new ProfesorDao();
    }

    public void registrarProfesor(Profesor profesor) throws Exception {
        if (profesor.getNombre() == null || profesor.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del profesor no puede estar vacío.");
        }
        if (profesor.getApellidos() == null || profesor.getApellidos().trim().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos no pueden estar vacíos.");
        }

        int anioActual = LocalDate.now().getYear();
        if (profesor.getAnioNacimiento() < 1900 || profesor.getAnioNacimiento() > (anioActual - 18)) {
            throw new IllegalArgumentException("El año de nacimiento no es válido o el profesor es menor de edad.");
        }

        try {
            int idGenerado = profesorDao.insert(profesor);
            if (idGenerado == 0) {
                throw new Exception("No se pudo registrar el profesor en la base de datos.");
            }
        } catch (SQLException e) {
            throw new Exception("Error de base de datos al registrar el profesor: " + e.getMessage());
        }
    }

    public List<Profesor> obtenerTodosLosProfesores() throws Exception {
        try {
            return profesorDao.findAll();
        } catch (SQLException e) {
            throw new Exception("Error al obtener los profesores: " + e.getMessage());
        }
    }
}