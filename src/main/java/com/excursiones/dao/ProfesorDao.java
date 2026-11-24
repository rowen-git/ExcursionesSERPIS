package com.excursiones.dao;

import com.excursiones.model.Profesor;
import com.excursiones.connection.ConexionBDD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfesorDao implements DAO<Profesor, Integer> {

    @Override
    public Integer insert(Profesor profesor) throws SQLException {
        String sql = "INSERT INTO profesor (nombre, apellidos, anio_nacimiento, telefono) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            sentencia.setString(1, profesor.getNombre());
            sentencia.setString(2, profesor.getApellidos());
            sentencia.setInt(3, profesor.getAnioNacimiento());
            sentencia.setString(4, profesor.getTelefono());
            
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
    public Optional<Profesor> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM profesor WHERE id_profesor = ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    Profesor profesor = new Profesor(
                            rs.getInt("id_profesor"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getInt("anio_nacimiento"),
                            rs.getString("telefono")
                    );
                    return Optional.of(profesor);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Profesor> findAll() throws SQLException {
        List<Profesor> profesores = new ArrayList<>();
        String sql = "SELECT * FROM profesor";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet rs = sentencia.executeQuery()) {
            
            while (rs.next()) {
                Profesor profesor = new Profesor(
                        rs.getInt("id_profesor"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getInt("anio_nacimiento"),
                        rs.getString("telefono")
                );
                profesores.add(profesor);
            }
        }
        return profesores;
    }

    @Override
    public boolean update(Profesor profesor) throws SQLException {
        String sql = "UPDATE profesor SET nombre = ?, apellidos = ?, anio_nacimiento = ?, telefono = ? WHERE id_profesor = ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            
            sentencia.setString(1, profesor.getNombre());
            sentencia.setString(2, profesor.getApellidos());
            sentencia.setInt(3, profesor.getAnioNacimiento());
            sentencia.setString(4, profesor.getTelefono());
            sentencia.setInt(5, profesor.getIdProfesor());
            
            return sentencia.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM profesor WHERE id_profesor = ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            
            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        }
    }
}