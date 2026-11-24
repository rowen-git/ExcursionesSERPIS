package com.excursiones.dao;

import com.excursiones.model.Excursion;
import com.excursiones.connection.ConexionBDD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExcursionDao implements DAO<Excursion, Integer> {

    @Override
    public Integer insert(Excursion excursion) throws SQLException {
        String sql = "INSERT INTO excursion (destino, duracion_horas, fecha, precio, finalizada, id_grupo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBDD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, excursion.getDestino());
            sentencia.setInt(2, excursion.getDuracionHoras());
            sentencia.setDate(3, Date.valueOf(excursion.getFecha()));
            sentencia.setDouble(4, excursion.getPrecio());
            sentencia.setBoolean(5, excursion.isFinalizada());
            sentencia.setInt(6, excursion.getIdGrupo());

            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet llavesGeneradas = sentencia.getGeneratedKeys()) {
                    if (llavesGeneradas.next()) {
                        return llavesGeneradas.getInt(1);
                    }
                }
            }
            return 0;
        }
    }

    @Override
    public Optional<Excursion> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM excursion WHERE id_excursion = ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    Excursion excursion = new Excursion(
                            rs.getInt("id_excursion"),
                            rs.getString("destino"),
                            rs.getInt("duracion_horas"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getDouble("precio"),
                            rs.getBoolean("finalizada"),
                            rs.getInt("id_grupo"));
                    return Optional.of(excursion);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Excursion> findAll() throws SQLException {
        List<Excursion> excursiones = new ArrayList<>();
        String sql = "SELECT * FROM excursion";
        try (Connection conexion = ConexionBDD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet rs = sentencia.executeQuery()) {

            while (rs.next()) {
                Excursion excursion = new Excursion(
                        rs.getInt("id_excursion"),
                        rs.getString("destino"),
                        rs.getInt("duracion_horas"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDouble("precio"),
                        rs.getBoolean("finalizada"),
                        rs.getInt("id_grupo"));
                excursiones.add(excursion);
            }
        }
        return excursiones;
    }

    @Override
    public boolean update(Excursion excursion) throws SQLException {
        String sql = "UPDATE excursion SET destino = ?, duracion_horas = ?, fecha = ?, precio = ?, finalizada = ?, id_grupo = ? WHERE id_excursion = ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, excursion.getDestino());
            sentencia.setInt(2, excursion.getDuracionHoras());
            sentencia.setDate(3, Date.valueOf(excursion.getFecha()));
            sentencia.setDouble(4, excursion.getPrecio());
            sentencia.setBoolean(5, excursion.isFinalizada());
            sentencia.setInt(6, excursion.getIdGrupo());
            sentencia.setInt(7, excursion.getIdExcursion());

            return sentencia.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sqlIntermedia = "DELETE FROM excursion_profesor WHERE id_excursion = ?";
        String sqlExcursion = "DELETE FROM excursion WHERE id_excursion = ?";

        try (Connection conexion = ConexionBDD.obtenerConexion()) {

            try (PreparedStatement stmtIntermedia = conexion.prepareStatement(sqlIntermedia)) {
                stmtIntermedia.setInt(1, id);
                stmtIntermedia.executeUpdate();
            }

            try (PreparedStatement stmtExcursion = conexion.prepareStatement(sqlExcursion)) {
                stmtExcursion.setInt(1, id);
                return stmtExcursion.executeUpdate() > 0;
            }
        }
    }

    public List<String> obtenerExcursionesConProfesores() throws SQLException {
        List<String> reporte = new ArrayList<>();

        String sql = "SELECT e.id_excursion, e.destino, e.fecha, g.nombre_curso, " +
                "GROUP_CONCAT(CONCAT(p.nombre, ' ', p.apellidos) SEPARATOR ', ') AS profesores " +
                "FROM excursion e " +
                "LEFT JOIN grupo g ON e.id_grupo = g.id_grupo " +
                "LEFT JOIN excursion_profesor ep ON e.id_excursion = ep.id_excursion " +
                "LEFT JOIN profesor p ON ep.id_profesor = p.id_profesor " +
                "GROUP BY e.id_excursion, e.destino, e.fecha, g.nombre_curso " +
                "ORDER BY e.id_excursion";

        try (Connection conexion = ConexionBDD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet rs = sentencia.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_excursion");
                String destino = rs.getString("destino");
                java.sql.Date fecha = rs.getDate("fecha");
                String grupo = rs.getString("nombre_curso");
                String profesores = rs.getString("profesores");

                if (grupo == null) {
                    grupo = "Sin grupo";
                }

                if (profesores == null || profesores.trim().isEmpty()) {
                    profesores = "Sin profesores asignados";
                }

                
                reporte.add(String.format("ID: %d | Destino: %-15s | Fecha: %-10s | Grupo: %-10s | Profesores: %s",
                        id, destino, (fecha != null ? fecha.toString() : "N/A"), grupo, profesores));
            }
        }
        return reporte;
    }

    public List<Excursion> obtenerExcursionesFinalizadasEntreFechas(LocalDate inicio, LocalDate fin)
            throws SQLException {
        List<Excursion> lista = new ArrayList<>();
        String sql = "SELECT * FROM excursion WHERE finalizada = true AND fecha BETWEEN ? AND ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setDate(1, java.sql.Date.valueOf(inicio));
            sentencia.setDate(2, java.sql.Date.valueOf(fin));

            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Excursion e = new Excursion(
                            rs.getInt("id_excursion"),
                            rs.getString("destino"),
                            rs.getInt("duracion_horas"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getDouble("precio"),
                            rs.getBoolean("finalizada"),
                            rs.getInt("id_grupo"));
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    public void asignarProfesoresAExcursion(int idExcursion, List<Integer> idsProfesores) throws SQLException {
        String sql = "INSERT INTO excursion_profesor (id_excursion, id_profesor) VALUES (?, ?)";
        try (Connection conexion = ConexionBDD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            for (Integer idProfesor : idsProfesores) {
                sentencia.setInt(1, idExcursion);
                sentencia.setInt(2, idProfesor);
                sentencia.addBatch();
            }
            sentencia.executeBatch();
        }
    }

    public boolean tieneExcursionesAsociadas(int idGrupo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM excursion WHERE id_grupo = ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idGrupo);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    public List<Excursion> obtenerExcursionesFinalizadas() throws SQLException {
    List<Excursion> lista = new ArrayList<>();
    String sql = "SELECT * FROM excursion WHERE finalizada = true ORDER BY fecha DESC";
    try (Connection conexion = ConexionBDD.obtenerConexion();
         PreparedStatement sentencia = conexion.prepareStatement(sql);
         ResultSet rs = sentencia.executeQuery()) {

        while (rs.next()) {
            Excursion e = new Excursion(
                    rs.getInt("id_excursion"),
                    rs.getString("destino"),
                    rs.getInt("duracion_horas"),
                    rs.getDate("fecha").toLocalDate(),
                    rs.getDouble("precio"),
                    rs.getBoolean("finalizada"),
                    rs.getInt("id_grupo"));
            lista.add(e);
        }
    }
    return lista;
}
}