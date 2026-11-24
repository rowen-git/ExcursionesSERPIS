package com.excursiones.service;

import com.excursiones.dao.ExcursionDao;
import com.excursiones.dao.ResenaDao;
import com.excursiones.model.Actividad;
import com.excursiones.model.Excursion;
import com.excursiones.model.Resena;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import org.bson.types.ObjectId;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResenaServicio {

    private final ResenaDao resenaDao;
    private final ExcursionDao excursionDao;

    public ResenaServicio() {
        this.resenaDao = new ResenaDao();
        this.excursionDao = new ExcursionDao();
    }

    public void verificarExcursionParaAltaResena(int idExcursion) throws Exception {
        if (idExcursion <= 0) {
            throw new IllegalArgumentException("El ID de la excursión debe ser mayor que 0.");
        }

        Excursion excursion;
        try {
            excursion = excursionDao.findById(idExcursion)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No existe ninguna excursión con ID " + idExcursion + " en MySQL."));
        } catch (SQLException e) {
            throw new Exception("Error al consultar MySQL: " + e.getMessage());
        }

        if (!excursion.isFinalizada()) {
            throw new IllegalArgumentException("Solo se puede crear una reseña de una excursión finalizada.");
        }

        try {
            if (resenaDao.existePorIdExcursion(idExcursion)) {
                throw new IllegalArgumentException(
                        "La excursión con ID " + idExcursion + " ya tiene una reseña registrada.");
            }
        } catch (MongoException e) {
            throw new Exception("Error al consultar MongoDB: " + e.getMessage());
        }
    }

    public String registrarResena(Resena resena) throws Exception {
        if (resena == null) {
            throw new IllegalArgumentException("La reseña no puede ser nula.");
        }

        verificarExcursionParaAltaResena(resena.getIdExcursion());
        validarValoracion(resena.getValoracionGlobal(), "La valoración global");
        validarTextoObligatorio(resena.getComentarioGeneral(), "El comentario general");

        List<Actividad> actividades = resena.getActividades();
        if (actividades == null || actividades.isEmpty()) {
            throw new IllegalArgumentException("Cada reseña debe contener al menos una actividad realizada.");
        }
        for (Actividad actividad : actividades) {
            validarActividad(actividad);
        }

        List<String> incidenciasLimpias = new ArrayList<>();
        if (resena.getIncidencias() != null) {
            for (String inc : resena.getIncidencias()) {
                if (inc != null && !inc.trim().isEmpty()) {
                    incidenciasLimpias.add(inc.trim());
                }
            }
        }
        resena.setIncidencias(incidenciasLimpias);
        resena.setComentarioGeneral(resena.getComentarioGeneral().trim());
        resena.setFechaCreacion(LocalDateTime.now());

        try {
            ObjectId idGenerado = resenaDao.insertar(resena);
            return idGenerado.toHexString();
        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new IllegalArgumentException("Ya existe una reseña registrada para esta excursión.");
            }
            throw new Exception("Error al guardar la reseña en MongoDB: " + e.getMessage());
        } catch (MongoException e) {
            throw new Exception("Error al guardar la reseña en MongoDB: " + e.getMessage());
        }
    }

    public Resena consultarPorIdExcursion(int idExcursion) throws Exception {
        if (idExcursion <= 0) {
            throw new IllegalArgumentException("El ID de la excursión debe ser mayor que 0.");
        }
        try {
            return resenaDao.buscarPorIdExcursion(idExcursion)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No existe ninguna reseña asociada a la excursión con ID " + idExcursion + "."));
        } catch (MongoException e) {
            throw new Exception("Error al consultar la reseña en MongoDB: " + e.getMessage());
        }
    }

    public boolean existeResena(int idExcursion) throws Exception {
        if (idExcursion <= 0) {
            return false;
        }
        try {
            return resenaDao.existePorIdExcursion(idExcursion);
        } catch (MongoException e) {
            throw new Exception("Error al comprobar la existencia de la reseña: " + e.getMessage());
        }
    }

    public void modificarDatosGenerales(int idExcursion, int nuevaValoracion, String nuevoComentario) throws Exception {
        consultarPorIdExcursion(idExcursion);
        validarValoracion(nuevaValoracion, "La valoración global");
        validarTextoObligatorio(nuevoComentario, "El comentario general");

        try {
            boolean actualizada = resenaDao.actualizarDatosGenerales(
                    idExcursion, nuevaValoracion, nuevoComentario.trim());
            if (!actualizada) {
                throw new Exception("No se pudo actualizar la reseña.");
            }
        } catch (MongoException e) {
            throw new Exception("Error al modificar la reseña en MongoDB: " + e.getMessage());
        }
    }

    public void agregarActividad(int idExcursion, Actividad actividad) throws Exception {
        consultarPorIdExcursion(idExcursion);
        validarActividad(actividad);

        try {
            boolean agregada = resenaDao.agregarActividad(idExcursion, actividad);
            if (!agregada) {
                throw new Exception("No se pudo añadir la actividad.");
            }
        } catch (MongoException e) {
            throw new Exception("Error al añadir actividad en MongoDB: " + e.getMessage());
        }
    }

    public void modificarActividad(int idExcursion, int numeroActividad, Actividad nuevaActividad) throws Exception {
        Resena resena = consultarPorIdExcursion(idExcursion);
        validarActividad(nuevaActividad);

        if (numeroActividad < 1 || numeroActividad > resena.getActividades().size()) {
            throw new IllegalArgumentException("El número de actividad indicado no es válido.");
        }

        try {
            boolean modificada = resenaDao.modificarActividad(idExcursion, numeroActividad - 1, nuevaActividad);
            if (!modificada) {
                throw new Exception("No se pudo modificar la actividad.");
            }
        } catch (MongoException e) {
            throw new Exception("Error al modificar la actividad en MongoDB: " + e.getMessage());
        }
    }

    public void eliminarActividad(int idExcursion, int numeroActividad) throws Exception {
        Resena resena = consultarPorIdExcursion(idExcursion);

        if (resena.getActividades().size() <= 1) {
            throw new IllegalArgumentException(
                    "No se puede eliminar la última actividad. Toda reseña debe tener al menos una actividad.");
        }

        if (numeroActividad < 1 || numeroActividad > resena.getActividades().size()) {
            throw new IllegalArgumentException("El número de actividad indicado no es válido.");
        }

        try {
            boolean eliminada = resenaDao.eliminarActividad(idExcursion, numeroActividad - 1);
            if (!eliminada) {
                throw new Exception("No se pudo eliminar la actividad.");
            }
        } catch (MongoException e) {
            throw new Exception("Error al eliminar la actividad en MongoDB: " + e.getMessage());
        }
    }

    public void agregarIncidencia(int idExcursion, String incidencia) throws Exception {
        consultarPorIdExcursion(idExcursion);
        validarTextoObligatorio(incidencia, "La descripción de la incidencia");

        try {
            boolean agregada = resenaDao.agregarIncidencia(idExcursion, incidencia.trim());
            if (!agregada) {
                throw new Exception("No se pudo añadir la incidencia.");
            }
        } catch (MongoException e) {
            throw new Exception("Error al añadir la incidencia en MongoDB: " + e.getMessage());
        }
    }

    public void modificarIncidencia(int idExcursion, int numeroIncidencia, String nuevaIncidencia) throws Exception {
        Resena resena = consultarPorIdExcursion(idExcursion);
        validarTextoObligatorio(nuevaIncidencia, "La descripción de la incidencia");

        if (resena.getIncidencias().isEmpty()) {
            throw new IllegalArgumentException("Esta reseña no tiene incidencias registradas.");
        }
        if (numeroIncidencia < 1 || numeroIncidencia > resena.getIncidencias().size()) {
            throw new IllegalArgumentException("El número de incidencia indicado no es válido.");
        }

        try {
            boolean modificada = resenaDao.modificarIncidencia(
                    idExcursion, numeroIncidencia - 1, nuevaIncidencia.trim());
            if (!modificada) {
                throw new Exception("No se pudo modificar la incidencia.");
            }
        } catch (MongoException e) {
            throw new Exception("Error al modificar la incidencia en MongoDB: " + e.getMessage());
        }
    }

    public void eliminarIncidencia(int idExcursion, int numeroIncidencia) throws Exception {
        Resena resena = consultarPorIdExcursion(idExcursion);

        if (resena.getIncidencias().isEmpty()) {
            throw new IllegalArgumentException("Esta reseña no tiene incidencias registradas.");
        }
        if (numeroIncidencia < 1 || numeroIncidencia > resena.getIncidencias().size()) {
            throw new IllegalArgumentException("El número de incidencia indicado no es válido.");
        }

        try {
            boolean eliminada = resenaDao.eliminarIncidencia(idExcursion, numeroIncidencia - 1);
            if (!eliminada) {
                throw new Exception("No se pudo eliminar la incidencia.");
            }
        } catch (MongoException e) {
            throw new Exception("Error al eliminar la incidencia en MongoDB: " + e.getMessage());
        }
    }

    public void eliminarResena(int idExcursion) throws Exception {
        consultarPorIdExcursion(idExcursion);
        try {
            boolean eliminada = resenaDao.eliminar(idExcursion);
            if (!eliminada) {
                throw new Exception("No se pudo eliminar la reseña.");
            }
        } catch (MongoException e) {
            throw new Exception("Error al eliminar la reseña en MongoDB: " + e.getMessage());
        }
    }

    public List<Resena> listarTodas() throws Exception {
        try {
            return resenaDao.listarTodasOrdenadasPorFechaDesc();
        } catch (MongoException e) {
            throw new Exception("Error al listar las reseñas: " + e.getMessage());
        }
    }

    public List<Resena> buscarPorValoracionGlobal(int valoracion) throws Exception {
        validarValoracion(valoracion, "La valoración global");
        try {
            return resenaDao.buscarPorValoracionGlobal(valoracion);
        } catch (MongoException e) {
            throw new Exception("Error al buscar reseñas por valoración: " + e.getMessage());
        }
    }

    public List<Resena> buscarPorActividadInferiorA(int valoracionLimite) throws Exception {
        validarValoracion(valoracionLimite, "La valoración límite");
        try {
            return resenaDao.buscarConActividadValoracionInferiorA(valoracionLimite);
        } catch (MongoException e) {
            throw new Exception("Error al buscar reseñas por valoración de actividades: " + e.getMessage());
        }
    }

    public List<Resena> listarConIncidencias() throws Exception {
        try {
            return resenaDao.listarConIncidencias();
        } catch (MongoException e) {
            throw new Exception("Error al listar reseñas con incidencias: " + e.getMessage());
        }
    }

    public Optional<Excursion> obtenerExcursionPorId(int idExcursion) throws Exception {
        try {
            return excursionDao.findById(idExcursion);
        } catch (SQLException e) {
            throw new Exception("Error al obtener datos de la excursión: " + e.getMessage());
        }
    }

    private void validarActividad(Actividad actividad) {
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no puede ser nula.");
        }
        validarTextoObligatorio(actividad.getNombre(), "El nombre de la actividad");
        actividad.setNombre(actividad.getNombre().trim());
        validarValoracion(actividad.getValoracion(), "La valoración de la actividad");
    }

    private void validarValoracion(int valoracion, String campo) {
        if (valoracion < 1 || valoracion > 5) {
            throw new IllegalArgumentException(campo + " debe estar comprendida entre 1 y 5.");
        }
    }

    private void validarTextoObligatorio(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
    }
}