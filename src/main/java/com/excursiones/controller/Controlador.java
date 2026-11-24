package com.excursiones.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.excursiones.model.Actividad;
import com.excursiones.model.Excursion;
import com.excursiones.model.Grupo;
import com.excursiones.model.Profesor;
import com.excursiones.model.Resena;
import com.excursiones.service.ExcursionServicio;
import com.excursiones.service.GrupoServicio;
import com.excursiones.service.ProfesorServicio;
import com.excursiones.service.ResenaServicio;

public class Controlador {

    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ProfesorServicio profesorServicio;
    private final GrupoServicio grupoServicio;
    private final ExcursionServicio excursionServicio;
    private final ResenaServicio resenaServicio;

    public Controlador() {
        this.profesorServicio = new ProfesorServicio();
        this.grupoServicio = new GrupoServicio();
        this.excursionServicio = new ExcursionServicio();
        this.resenaServicio = new ResenaServicio();
    }


    public String registrarProfesor(String nombre, String apellidos, int anioNacimiento, String telefono) {
        try {
            Profesor nuevoProfesor = new Profesor(nombre, apellidos, anioNacimiento, telefono);
            profesorServicio.registrarProfesor(nuevoProfesor);
            return "Éxito: Profesor registrado correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String listarProfesores() {
        try {
            List<Profesor> profesores = profesorServicio.obtenerTodosLosProfesores();
            if (profesores.isEmpty()) {
                return "No hay profesores registrados.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-5s | %-15s | %-20s%n", "ID", "NOMBRE", "APELLIDOS"));
            sb.append("----------------------------------------------------\n");
            for (Profesor p : profesores) {
                sb.append(String.format("%-5d | %-15s | %-20s%n",
                        p.getIdProfesor(), p.getNombre(), p.getApellidos()));
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al obtener profesores: " + e.getMessage();
        }
    }

    public String registrarGrupo(String nombreCurso, String etapa, int numAlumnos) {
        try {
            Grupo nuevoGrupo = new Grupo(nombreCurso, etapa, numAlumnos);
            grupoServicio.registrarGrupo(nuevoGrupo);
            return "Éxito: Grupo registrado correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String listarGrupos() {
        try {
            List<Grupo> grupos = grupoServicio.obtenerTodosLosGrupos();
            if (grupos.isEmpty()) {
                return "No hay grupos registrados.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-5s | %-10s | %-15s | %-10s%n", "ID", "CURSO", "ETAPA", "ALUMNOS"));
            sb.append("----------------------------------------------------\n");
            for (Grupo g : grupos) {
                sb.append(String.format("%-5d | %-10s | %-15s | %-10d%n",
                        g.getIdGrupo(), g.getNombreCurso(), g.getEtapa(), g.getNumAlumnos()));
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al obtener grupos: " + e.getMessage();
        }
    }

    public String eliminarGrupo(int idGrupo) {
        try {
            grupoServicio.eliminarGrupo(idGrupo);
            return "Éxito: Grupo eliminado correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String registrarExcursion(String destino, int duracion, LocalDate fecha, double precio, int idGrupo,
                                     List<Integer> idsProfesores) {
        try {
            Excursion nueva = new Excursion(destino, duracion, fecha, precio, false, idGrupo);
            excursionServicio.registrarExcursion(nueva, idsProfesores);
            return "Éxito: Excursión registrada correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String listarExcursiones() {
        try {
            List<Excursion> lista = excursionServicio.obtenerTodas();
            if (lista.isEmpty()) {
                return "No hay excursiones registradas.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-5s | %-20s | %-12s | %-10s%n", "ID", "DESTINO", "FECHA", "ESTADO"));
            sb.append("----------------------------------------------------------\n");
            for (Excursion e : lista) {
                String estado = e.isFinalizada() ? "Finalizada" : "Pendiente";
                sb.append(String.format("%-5d | %-20s | %-12s | %-10s%n",
                        e.getIdExcursion(), e.getDestino(), e.getFecha().toString(), estado));
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al listar: " + e.getMessage();
        }
    }

    public String eliminarExcursion(int id) {
        try {
            excursionServicio.eliminarExcursion(id);
            return "Éxito: Excursión eliminada.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String modificarFecha(int id, LocalDate nuevaFecha) {
        try {
            excursionServicio.modificarFechaExcursion(id, nuevaFecha);
            return "Éxito: Fecha modificada correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String finalizarExcursion(int id) {
        try {
            excursionServicio.finalizarExcursion(id);
            return "Éxito: Excursión marcada como finalizada.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String listarExcursionesConProfesores() {
        try {
            List<String> reporte = excursionServicio.obtenerExcursionesConProfesores();
            if (reporte.isEmpty()) {
                return "No hay excursiones registradas para mostrar.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- LISTADO TOTAL DE EXCURSIONES Y PROFESORES ACOMPAÑANTES ---\n");
            for (String linea : reporte) {
                sb.append(linea).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al generar el listado: " + e.getMessage();
        }
    }

    public String listarExcursionesFinalizadas(LocalDate inicio, LocalDate fin) {
        try {
            List<Excursion> lista = excursionServicio.obtenerFinalizadasEntreFechas(inicio, fin);
            if (lista.isEmpty()) {
                return "No hay excursiones finalizadas en este rango de fechas.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-5s | %-20s | %-12s%n", "ID", "DESTINO", "FECHA"));
            sb.append("---------------------------------------------\n");
            for (Excursion e : lista) {
                sb.append(String.format("%-5d | %-20s | %-12s%n",
                        e.getIdExcursion(), e.getDestino(), e.getFecha().toString()));
            }
            return sb.toString();
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    // MONGODB

    public String listarExcursionesFinalizadasParaResena() {
        try {
            List<Excursion> finalizadas = excursionServicio.obtenerFinalizadas();
            if (finalizadas.isEmpty()) {
                return "No hay excursiones finalizadas disponibles.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-6s | %-22s | %-12s | %-12s%n", "ID EXC", "DESTINO", "FECHA", "TIENE RESEÑA"));
            sb.append("----------------------------------------------------------------\n");
            for (Excursion e : finalizadas) {
                String tieneResena = resenaServicio.existeResena(e.getIdExcursion()) ? "Sí" : "No";
                sb.append(String.format("%-6d | %-22s | %-12s | %-12s%n",
                        e.getIdExcursion(), e.getDestino(), e.getFecha().toString(), tieneResena));
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al obtener excursiones finalizadas: " + e.getMessage();
        }
    }

    public String listarExcursionesConResena() {
        try {
            List<Resena> resenas = resenaServicio.listarTodas();
            if (resenas.isEmpty()) {
                return "No hay reseñas registradas en MongoDB.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-6s | %-22s | %-16s | %-11s%n",
                    "ID EXC", "DESTINO", "FECHA RESEÑA", "VAL. GLOBAL"));
            sb.append("-------------------------------------------------------------------\n");
            for (Resena r : resenas) {
                String destino = obtenerDestinoExcursion(r.getIdExcursion());
                String fechaStr = r.getFechaCreacion() != null ? r.getFechaCreacion().format(FORMATO_FECHA_HORA) : "N/A";
                sb.append(String.format("%-6d | %-22s | %-16s | %d/5%n",
                        r.getIdExcursion(), destino, fechaStr, r.getValoracionGlobal()));
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al listar excursiones con reseña: " + e.getMessage();
        }
    }

    public String validarExcursionParaAltaResena(int idExcursion) {
        try {
            resenaServicio.verificarExcursionParaAltaResena(idExcursion);
            return null;
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public boolean existeResena(int idExcursion) {
        try {
            return resenaServicio.existeResena(idExcursion);
        } catch (Exception e) {
            return false;
        }
    }

    public String registrarResena(int idExcursion, int valoracionGlobal, String comentarioGeneral,
                                  List<Actividad> actividades, List<String> incidencias) {
        try {
            Resena resena = new Resena(idExcursion, valoracionGlobal, comentarioGeneral, actividades, incidencias);
            String idMongo = resenaServicio.registrarResena(resena);
            return "Éxito: Reseña registrada correctamente en MongoDB (ID documento: " + idMongo + ").";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String consultarResena(int idExcursion) {
        try {
            Resena resena = resenaServicio.consultarPorIdExcursion(idExcursion);
            return formatearResena(resena);
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String modificarDatosGeneralesResena(int idExcursion, int nuevaValoracion, String nuevoComentario) {
        try {
            resenaServicio.modificarDatosGenerales(idExcursion, nuevaValoracion, nuevoComentario);
            return "Éxito: Valoración global y comentario actualizados correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String agregarActividadResena(int idExcursion, String nombre, int valoracion) {
        try {
            resenaServicio.agregarActividad(idExcursion, new Actividad(nombre, valoracion));
            return "Éxito: Actividad añadida a la reseña.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String modificarActividadResena(int idExcursion, int numeroActividad, String nuevoNombre, int nuevaValoracion) {
        try {
            resenaServicio.modificarActividad(idExcursion, numeroActividad, new Actividad(nuevoNombre, nuevaValoracion));
            return "Éxito: Actividad modificada correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String eliminarActividadResena(int idExcursion, int numeroActividad) {
        try {
            resenaServicio.eliminarActividad(idExcursion, numeroActividad);
            return "Éxito: Actividad eliminada correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String agregarIncidenciaResena(int idExcursion, String descripcion) {
        try {
            resenaServicio.agregarIncidencia(idExcursion, descripcion);
            return "Éxito: Incidencia añadida a la reseña.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String modificarIncidenciaResena(int idExcursion, int numeroIncidencia, String nuevaDescripcion) {
        try {
            resenaServicio.modificarIncidencia(idExcursion, numeroIncidencia, nuevaDescripcion);
            return "Éxito: Incidencia modificada correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String eliminarIncidenciaResena(int idExcursion, int numeroIncidencia) {
        try {
            resenaServicio.eliminarIncidencia(idExcursion, numeroIncidencia);
            return "Éxito: Incidencia eliminada correctamente.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String eliminarResena(int idExcursion) {
        try {
            resenaServicio.eliminarResena(idExcursion);
            return "Éxito: Reseña eliminada correctamente de MongoDB.";
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String listarTodasLasResenas() {
        try {
            List<Resena> lista = resenaServicio.listarTodas();
            return formatearListaResenas(lista, "No hay reseñas registradas.");
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String buscarResenasPorValoracionGlobal(int valoracion) {
        try {
            List<Resena> lista = resenaServicio.buscarPorValoracionGlobal(valoracion);
            return formatearListaResenas(lista, "No hay reseñas con una valoración global de " + valoracion + ".");
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String buscarResenasConActividadInferiorA(int valoracionLimite) {
        try {
            List<Resena> lista = resenaServicio.buscarPorActividadInferiorA(valoracionLimite);
            return formatearListaResenas(lista,
                    "No hay reseñas con actividades valoradas por debajo de " + valoracionLimite + ".");
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    public String listarResenasConIncidencias() {
        try {
            List<Resena> lista = resenaServicio.listarConIncidencias();
            return formatearListaResenas(lista, "No hay reseñas con incidencias registradas.");
        } catch (Exception e) {
            return "Error crítico: " + e.getMessage();
        }
    }

    private String formatearListaResenas(List<Resena> lista, String mensajeVacio) {
        if (lista.isEmpty()) {
            return mensajeVacio;
        }
        StringBuilder sb = new StringBuilder();
        for (Resena r : lista) {
            sb.append(formatearResena(r)).append("\n");
        }
        return sb.toString();
    }

    private String formatearResena(Resena r) {
        StringBuilder sb = new StringBuilder();
        String destino = obtenerDestinoExcursion(r.getIdExcursion());
        String fechaExcursion = obtenerFechaExcursion(r.getIdExcursion());
        String fechaResena = r.getFechaCreacion() != null ? r.getFechaCreacion().format(FORMATO_FECHA_HORA) : "N/A";

        sb.append("============================================================\n");
        sb.append(String.format("Excursión ID: %d | Destino: %s | Fecha excursión: %s%n",
                r.getIdExcursion(), destino, fechaExcursion));
        sb.append(String.format("Fecha reseña: %s | Valoración global: %d/5%n",
                fechaResena, r.getValoracionGlobal()));
        sb.append("Comentario general: ").append(r.getComentarioGeneral()).append("\n");

        sb.append("Actividades realizadas:\n");
        List<Actividad> actividades = r.getActividades();
        if (actividades.isEmpty()) {
            sb.append("  (Sin actividades)\n");
        } else {
            for (int i = 0; i < actividades.size(); i++) {
                Actividad a = actividades.get(i);
                sb.append(String.format("  %d. %s - Valoración: %d/5%n",
                        i + 1, a.getNombre(), a.getValoracion()));
            }
        }

        sb.append("Incidencias:\n");
        List<String> incidencias = r.getIncidencias();
        if (incidencias.isEmpty()) {
            sb.append("  Sin incidencias registradas.\n");
        } else {
            for (int i = 0; i < incidencias.size(); i++) {
                sb.append(String.format("  %d. %s%n", i + 1, incidencias.get(i)));
            }
        }
        sb.append("============================================================");
        return sb.toString();
    }

    private String obtenerDestinoExcursion(int idExcursion) {
        try {
            Optional<Excursion> exc = resenaServicio.obtenerExcursionPorId(idExcursion);
            return exc.map(Excursion::getDestino).orElse("Desconocido");
        } catch (Exception e) {
            return "No disponible";
        }
    }

    private String obtenerFechaExcursion(int idExcursion) {
        try {
            Optional<Excursion> exc = resenaServicio.obtenerExcursionPorId(idExcursion);
            return exc.map(e -> e.getFecha().toString()).orElse("N/A");
        } catch (Exception e) {
            return "N/A";
        }
    }
}