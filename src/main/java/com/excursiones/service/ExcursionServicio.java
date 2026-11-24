package com.excursiones.service;

import com.excursiones.dao.ExcursionDao;
import com.excursiones.model.Excursion;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ExcursionServicio {

    private ExcursionDao excursionDao;

    public ExcursionServicio() {
        this.excursionDao = new ExcursionDao();
    }

    public void registrarExcursion(Excursion excursion, List<Integer> idsProfesores) throws Exception {
        if (idsProfesores == null || idsProfesores.isEmpty()) {
            throw new IllegalArgumentException("Es obligatorio asignar al menos un profesor acompañante.");
        }

        long diasAntelacion = ChronoUnit.DAYS.between(LocalDate.now(), excursion.getFecha());
        if (diasAntelacion < 10) {
            throw new IllegalArgumentException(
                    "La excursión debe crearse con al menos 10 días naturales de antelación.");
        }

        try {
            int idGenerado = excursionDao.insert(excursion);
            if (idGenerado == 0) {
                throw new Exception("No se pudo registrar la excursión.");
            }
            excursionDao.asignarProfesoresAExcursion(idGenerado, idsProfesores);
        } catch (SQLException e) {
            throw new Exception("Error de base de datos: " + e.getMessage());
        }
    }

    public List<Excursion> obtenerTodas() throws Exception {
        try {
            return excursionDao.findAll();
        } catch (SQLException e) {
            throw new Exception("Error al obtener excursiones: " + e.getMessage());
        }
    }

    public void eliminarExcursion(int idExcursion) throws Exception {

        Excursion excursion = excursionDao.findById(idExcursion)
                .orElseThrow(() -> new IllegalArgumentException("La excursión no existe."));

        if (excursion.isFinalizada()) {
            throw new IllegalArgumentException("No se puede eliminar una excursión que ya está finalizada.");
        }

        try {
            boolean eliminado = excursionDao.delete(idExcursion);
            if (!eliminado) {
                throw new Exception("No se pudo eliminar la excursión.");
            }
        } catch (SQLException e) {
            throw new Exception("Error al eliminar la excursión: " + e.getMessage());
        }
    }

    public void modificarFechaExcursion(int idExcursion, LocalDate nuevaFecha) throws Exception {
        Excursion excursion = excursionDao.findById(idExcursion)
                .orElseThrow(() -> new IllegalArgumentException("La excursión no existe."));

        if (excursion.isFinalizada()) {
            throw new IllegalArgumentException("No se puede modificar una excursión que ya está finalizada.");
        }

        long diasAntelacion = ChronoUnit.DAYS.between(LocalDate.now(), nuevaFecha);
        if (diasAntelacion < 10) {
            throw new IllegalArgumentException("La nueva fecha debe tener al menos 10 días de antelación.");
        }

        excursion.setFecha(nuevaFecha);
        try {
            excursionDao.update(excursion);
        } catch (SQLException e) {
            throw new Exception("Error al actualizar la excursión: " + e.getMessage());
        }
    }

    public void finalizarExcursion(int idExcursion) throws Exception {
        Excursion excursion = excursionDao.findById(idExcursion)
                .orElseThrow(() -> new IllegalArgumentException("La excursión no existe."));

        if (excursion.isFinalizada()) {
            throw new IllegalArgumentException("La excursión ya se encuentra finalizada.");
        }

        if (!excursion.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Solo se puede finalizar si la fecha de realización es anterior a la actual.");
        }

        excursion.setFinalizada(true);
        try {
            excursionDao.update(excursion);
        } catch (SQLException e) {
            throw new Exception("Error al finalizar la excursión: " + e.getMessage());
        }
    }

    public List<String> obtenerExcursionesConProfesores() throws Exception {
        try {
            return excursionDao.obtenerExcursionesConProfesores();
        } catch (SQLException e) {
            throw new Exception("Error al obtener el listado con profesores: " + e.getMessage());
        }
    }

    public List<Excursion> obtenerFinalizadasEntreFechas(LocalDate inicio, LocalDate fin) throws Exception {
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        try {
            return excursionDao.obtenerExcursionesFinalizadasEntreFechas(inicio, fin);
        } catch (SQLException e) {
            throw new Exception("Error en la base de datos: " + e.getMessage());
        }
    }
    public List<Excursion> obtenerFinalizadas() throws Exception {
    try {
        return excursionDao.obtenerExcursionesFinalizadas();
    } catch (SQLException e) {
        throw new Exception("Error al obtener las excursiones finalizadas: " + e.getMessage());
    }
}
}