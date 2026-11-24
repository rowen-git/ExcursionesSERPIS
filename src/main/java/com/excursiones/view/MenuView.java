package com.excursiones.view;

import com.excursiones.controller.Controlador;
import com.excursiones.model.Actividad;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuView {

    private final Controlador controlador;

    public MenuView() {
        this.controlador = new Controlador();
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        do {
            System.out.println("\n=== MENÚ PRINCIPAL - EXCURSIONES IES SERPIS ===");
            System.out.println("--- GESTIÓN DE EXCURSIONES (MySQL) ---");
            System.out.println("1. Alta de Profesor");
            System.out.println("2. Alta de grupo");
            System.out.println("3. Baja de grupo");
            System.out.println("4. Alta de excursión");
            System.out.println("5. Baja de excursión");
            System.out.println("6. Modificación excursión");
            System.out.println("7. Finalizar excursión");
            System.out.println("8. Listado total de excursiones con los nombres y apellidos de los profesores responsables");
            System.out.println("9. Listado de excursiones finalizadas entre dos fechas dadas");
            System.out.println("--- GESTIÓN DE RESEÑAS (MongoDB) ---");
            System.out.println("10. Gestión de reseñas de excursiones");
            System.out.println("--------------------------------------");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                switch (opcion) {
                    case 1:
                        System.out.println("\n--- ALTA DE PROFESOR ---");
                        System.out.print("Nombre: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Apellidos: ");
                        String apellidos = scanner.nextLine();
                        System.out.print("Año de nacimiento: ");
                        int anio = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Teléfono: ");
                        String telefono = scanner.nextLine();

                        System.out.println(controlador.registrarProfesor(nombre, apellidos, anio, telefono));
                        break;

                    case 2:
                        System.out.println("\n--- ALTA DE GRUPO ---");
                        System.out.print("Nombre del curso (ej. 1DAMC): ");
                        String nombreCurso = scanner.nextLine();
                        System.out.print("Etapa educativa (ej. ESO, Bachillerato, FP): ");
                        String etapa = scanner.nextLine();
                        System.out.print("Número de alumnos: ");
                        int numAlumnos = Integer.parseInt(scanner.nextLine().trim());

                        System.out.println(controlador.registrarGrupo(nombreCurso, etapa, numAlumnos));
                        break;

                    case 3:
                        System.out.println("\n--- BAJA DE GRUPO ---");
                        System.out.println(controlador.listarGrupos());
                        System.out.print("Introduce el ID del grupo a eliminar: ");
                        int idGrupo = Integer.parseInt(scanner.nextLine().trim());

                        System.out.println(controlador.eliminarGrupo(idGrupo));
                        break;

                    case 4:
                        System.out.println("\n--- ALTA DE EXCURSIÓN ---");
                        System.out.print("Destino: ");
                        String destino = scanner.nextLine();
                        System.out.print("Duración (horas): ");
                        int duracion = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Fecha (YYYY-MM-DD): ");
                        LocalDate fecha = LocalDate.parse(scanner.nextLine().trim());
                        System.out.print("Precio: ");
                        double precio = Double.parseDouble(scanner.nextLine().trim());

                        System.out.println("\n--- GRUPOS DISPONIBLES ---");
                        System.out.println(controlador.listarGrupos());
                        System.out.print("ID del grupo que realiza la excursión: ");
                        int idGrupoExc = Integer.parseInt(scanner.nextLine().trim());

                        System.out.println("\n--- PROFESORES DISPONIBLES ---");
                        System.out.println(controlador.listarProfesores());

                        List<Integer> idsProfesores = new ArrayList<>();
                        int idProfesor;
                        do {
                            System.out.print("Introduce el ID de un profesor acompañante (o 0 para terminar de añadir): ");
                            idProfesor = Integer.parseInt(scanner.nextLine().trim());
                            if (idProfesor != 0) {
                                idsProfesores.add(idProfesor);
                                System.out.println("Profesor añadido. Puedes añadir otro o teclear 0.");
                            }
                        } while (idProfesor != 0);

                        System.out.println(controlador.registrarExcursion(destino, duracion, fecha, precio, idGrupoExc, idsProfesores));
                        break;

                    case 5:
                        System.out.println("\n--- BAJA DE EXCURSIÓN ---");
                        System.out.println(controlador.listarExcursiones());
                        System.out.print("ID de la excursión a eliminar: ");
                        int idBajaExc = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println(controlador.eliminarExcursion(idBajaExc));
                        break;

                    case 6:
                        System.out.println("\n--- MODIFICAR FECHA EXCURSIÓN ---");
                        System.out.println(controlador.listarExcursiones());
                        System.out.print("ID de la excursión: ");
                        int idModExc = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Nueva fecha (YYYY-MM-DD): ");
                        LocalDate nuevaFecha = LocalDate.parse(scanner.nextLine().trim());
                        System.out.println(controlador.modificarFecha(idModExc, nuevaFecha));
                        break;

                    case 7:
                        System.out.println("\n--- FINALIZAR EXCURSIÓN ---");
                        System.out.println(controlador.listarExcursiones());
                        System.out.print("ID de la excursión a finalizar: ");
                        int idFinExc = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println(controlador.finalizarExcursion(idFinExc));
                        break;

                    case 8:
                        System.out.println(controlador.listarExcursionesConProfesores());
                        break;

                    case 9:
                        System.out.println("\n--- EXCURSIONES FINALIZADAS ENTRE FECHAS ---");
                        System.out.print("Fecha de inicio (YYYY-MM-DD): ");
                        LocalDate fechaInicio = LocalDate.parse(scanner.nextLine().trim());
                        System.out.print("Fecha de fin (YYYY-MM-DD): ");
                        LocalDate fechaFin = LocalDate.parse(scanner.nextLine().trim());
                        System.out.println(controlador.listarExcursionesFinalizadas(fechaInicio, fechaFin));
                        break;

                    case 10:
                        mostrarMenuResenas(scanner);
                        break;

                    case 0:
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm 'del día' dd/MM/yyyy");
                        System.out.println("Adiós. Sesión finalizada a las " + dtf.format(LocalDateTime.now()));
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número válido.");
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha incorrecto. Usa YYYY-MM-DD (ejemplo: 2026-11-20).");
            }

        } while (opcion != 0);

        scanner.close();
    }

    private void mostrarMenuResenas(Scanner scanner) {
        int opcion = -1;

        do {
            System.out.println("\n=== GESTIÓN DE RESEÑAS (MongoDB) ===");
            System.out.println("1. Alta de reseña");
            System.out.println("2. Consulta de la reseña de una excursión");
            System.out.println("3. Modificación de una reseña");
            System.out.println("4. Eliminación de una reseña");
            System.out.println("5. Listado de todas las reseñas (más recientes primero)");
            System.out.println("6. Búsqueda de reseñas por valoración global");
            System.out.println("7. Búsqueda de reseñas con actividades por debajo de una valoración");
            System.out.println("8. Listado de reseñas que tengan alguna incidencia");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                switch (opcion) {
                    case 1:
                        gestionarAltaResena(scanner);
                        break;

                    case 2:
                        System.out.println("\n--- CONSULTA DE RESEÑA ---");
                        System.out.println(controlador.listarExcursionesConResena());
                        System.out.print("Introduce el ID de la excursión a consultar: ");
                        int idConsulta = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println(controlador.consultarResena(idConsulta));
                        break;

                    case 3:
                        System.out.println("\n--- MODIFICACIÓN DE RESEÑA ---");
                        System.out.println(controlador.listarExcursionesConResena());
                        System.out.print("Introduce el ID de la excursión cuya reseña quieres modificar: ");
                        int idMod = Integer.parseInt(scanner.nextLine().trim());
                        if (!controlador.existeResena(idMod)) {
                            System.out.println("Error de validación: No existe ninguna reseña para la excursión con ID " + idMod + ".");
                        } else {
                            mostrarMenuModificacionResena(scanner, idMod);
                        }
                        break;

                    case 4:
                        System.out.println("\n--- ELIMINACIÓN DE RESEÑA ---");
                        System.out.println(controlador.listarExcursionesConResena());
                        System.out.print("Introduce el ID de la excursión cuya reseña quieres eliminar: ");
                        int idElim = Integer.parseInt(scanner.nextLine().trim());
                        if (!controlador.existeResena(idElim)) {
                            System.out.println("Error de validación: No existe ninguna reseña para la excursión con ID " + idElim + ".");
                        } else {
                            System.out.println(controlador.consultarResena(idElim));
                            System.out.print("¿Confirmas que deseas eliminar esta reseña? (s/n): ");
                            String confirmacion = scanner.nextLine().trim();
                            if (confirmacion.equalsIgnoreCase("s")) {
                                System.out.println(controlador.eliminarResena(idElim));
                            } else {
                                System.out.println("Operación cancelada.");
                            }
                        }
                        break;

                    case 5:
                        System.out.println("\n--- LISTADO DE TODAS LAS RESEÑAS ---");
                        System.out.println(controlador.listarTodasLasResenas());
                        break;

                    case 6:
                        System.out.println("\n--- BÚSQUEDA POR VALORACIÓN GLOBAL ---");
                        System.out.print("Introduce la valoración global a buscar (1 a 5): ");
                        int valoracionBuscar = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println(controlador.buscarResenasPorValoracionGlobal(valoracionBuscar));
                        break;

                    case 7:
                        System.out.println("\n--- BÚSQUEDA POR ACTIVIDADES CON VALORACIÓN INFERIOR ---");
                        System.out.print("Introduce la valoración límite (1 a 5): ");
                        int valoracionLimite = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println(controlador.buscarResenasConActividadInferiorA(valoracionLimite));
                        break;

                    case 8:
                        System.out.println("\n--- RESEÑAS CON INCIDENCIAS ---");
                        System.out.println(controlador.listarResenasConIncidencias());
                        break;

                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número entero válido.");
            }

        } while (opcion != 0);
    }

    private void gestionarAltaResena(Scanner scanner) {
        System.out.println("\n--- ALTA DE RESEÑA ---");
        System.out.println(controlador.listarExcursionesFinalizadasParaResena());
        System.out.print("Introduce el ID de la excursión finalizada: ");
        int idExcursion = Integer.parseInt(scanner.nextLine().trim());

        String errorValidacion = controlador.validarExcursionParaAltaResena(idExcursion);
        if (errorValidacion != null) {
            System.out.println(errorValidacion);
            return;
        }

        System.out.print("Valoración global de la excursión (1 a 5): ");
        int valoracionGlobal = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Comentario general: ");
        String comentarioGeneral = scanner.nextLine();

        List<Actividad> actividades = new ArrayList<>();
        System.out.println("\n--- ACTIVIDADES REALIZADAS (mínimo 1) ---");
        while (true) {
            System.out.print("Nombre de la actividad (escribe 0 para terminar): ");
            String nombreAct = scanner.nextLine().trim();
            if (nombreAct.equals("0")) {
                break;
            }
            System.out.print("Valoración de la actividad (1 a 5): ");
            int valAct = Integer.parseInt(scanner.nextLine().trim());
            actividades.add(new Actividad(nombreAct, valAct));
            System.out.println("Actividad añadida.");
        }

        List<String> incidencias = new ArrayList<>();
        System.out.println("\n--- INCIDENCIAS (opcional) ---");
        while (true) {
            System.out.print("Descripción de la incidencia (escribe 0 para terminar): ");
            String incidencia = scanner.nextLine().trim();
            if (incidencia.equals("0")) {
                break;
            }
            if (!incidencia.isEmpty()) {
                incidencias.add(incidencia);
                System.out.println("Incidencia añadida.");
            }
        }

        System.out.println(controlador.registrarResena(
                idExcursion, valoracionGlobal, comentarioGeneral, actividades, incidencias));
    }

    private void mostrarMenuModificacionResena(Scanner scanner, int idExcursion) {
        int subOpcion = -1;

        do {
            System.out.println("\n--- ESTADO ACTUAL DE LA RESEÑA ---");
            System.out.println(controlador.consultarResena(idExcursion));
            System.out.println("\n¿Qué deseas modificar? (ID de excursión y fecha de creación no modificables)");
            System.out.println("1. Modificar valoración global y comentario general");
            System.out.println("2. Añadir una actividad");
            System.out.println("3. Modificar una actividad existente");
            System.out.println("4. Eliminar una actividad");
            System.out.println("5. Añadir una incidencia");
            System.out.println("6. Modificar una incidencia existente");
            System.out.println("7. Eliminar una incidencia");
            System.out.println("0. Finalizar modificación");
            System.out.print("Elige una opción: ");

            try {
                subOpcion = Integer.parseInt(scanner.nextLine().trim());
                switch (subOpcion) {
                    case 1:
                        System.out.print("Nueva valoración global (1 a 5): ");
                        int nuevaVal = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Nuevo comentario general: ");
                        String nuevoCom = scanner.nextLine();
                        System.out.println(controlador.modificarDatosGeneralesResena(idExcursion, nuevaVal, nuevoCom));
                        break;

                    case 2:
                        System.out.print("Nombre de la nueva actividad: ");
                        String nombreAct = scanner.nextLine();
                        System.out.print("Valoración de la actividad (1 a 5): ");
                        int valAct = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println(controlador.agregarActividadResena(idExcursion, nombreAct, valAct));
                        break;

                    case 3:
                        System.out.print("Número de actividad a modificar: ");
                        int numModAct = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Nuevo nombre de la actividad: ");
                        String nuevoNombreAct = scanner.nextLine();
                        System.out.print("Nueva valoración (1 a 5): ");
                        int nuevaValAct = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println(controlador.modificarActividadResena(
                                idExcursion, numModAct, nuevoNombreAct, nuevaValAct));
                        break;

                    case 4:
                        System.out.print("Número de actividad a eliminar: ");
                        int numElimAct = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println(controlador.eliminarActividadResena(idExcursion, numElimAct));
                        break;

                    case 5:
                        System.out.print("Descripción de la nueva incidencia: ");
                        String nuevaInc = scanner.nextLine();
                        System.out.println(controlador.agregarIncidenciaResena(idExcursion, nuevaInc));
                        break;

                    case 6:
                        System.out.print("Número de incidencia a modificar: ");
                        int numModInc = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Nueva descripción de la incidencia: ");
                        String descModInc = scanner.nextLine();
                        System.out.println(controlador.modificarIncidenciaResena(idExcursion, numModInc, descModInc));
                        break;

                    case 7:
                        System.out.print("Número de incidencia a eliminar: ");
                        int numElimInc = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println(controlador.eliminarIncidenciaResena(idExcursion, numElimInc));
                        break;

                    case 0:
                        System.out.println("Modificación finalizada.");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número entero válido.");
            }

        } while (subOpcion != 0);
    }
}