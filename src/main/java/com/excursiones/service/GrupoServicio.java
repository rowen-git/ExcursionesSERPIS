package com.excursiones.service;

import com.excursiones.dao.GrupoDao;
import com.excursiones.dao.ExcursionDao;
import com.excursiones.model.Grupo;
import java.sql.SQLException;
import java.util.List;

public class GrupoServicio {

    private GrupoDao grupoDao;
    private ExcursionDao excursionDao;

    public GrupoServicio() {
        this.grupoDao = new GrupoDao();
        this.excursionDao = new ExcursionDao();
    }

    public void registrarGrupo(Grupo grupo) throws Exception {
        if (grupo.getNombreCurso() == null || grupo.getNombreCurso().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso no puede estar vacío.");
        }
        if (grupo.getEtapa() == null || grupo.getEtapa().trim().isEmpty()) {
            throw new IllegalArgumentException("La etapa educativa no puede estar vacía.");
        }
        if (grupo.getNumAlumnos() <= 0) {
            throw new IllegalArgumentException("El número de alumnos debe ser mayor a cero.");
        }

        try {
            int idGenerado = grupoDao.insert(grupo);
            if (idGenerado == 0) {
                throw new Exception("No se pudo registrar el grupo en la base de datos.");
            }
        } catch (SQLException e) {
            throw new Exception("Error de base de datos al registrar el grupo: " + e.getMessage());
        }
    }

    public List<Grupo> obtenerTodosLosGrupos() throws Exception {
        try {
            return grupoDao.findAll();
        } catch (SQLException e) {
            throw new Exception("Error al obtener los grupos: " + e.getMessage());
        }
    }

    public void eliminarGrupo(int idGrupo) throws Exception {
        if (idGrupo <= 0) {
            throw new IllegalArgumentException("ID de grupo inválido.");
        }
        try {
            if (excursionDao.tieneExcursionesAsociadas(idGrupo)) {
                throw new IllegalArgumentException("No se puede eliminar el grupo porque tiene excursiones asociadas.");
            }

            boolean eliminado = grupoDao.delete(idGrupo);
            if (!eliminado) {
                throw new Exception("No se encontró el grupo con ID " + idGrupo + ".");
            }
        } catch (SQLException e) {
            throw new Exception("Error al eliminar el grupo: " + e.getMessage());
        }
    }
}