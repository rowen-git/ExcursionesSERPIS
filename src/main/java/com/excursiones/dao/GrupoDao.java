package com.excursiones.dao;

import com.excursiones.model.Grupo;
import com.excursiones.connection.ConexionBDD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GrupoDao implements DAO<Grupo, Integer> {

    @Override
    public Integer insert(Grupo grupo) throws SQLException {
        String sql = "INSERT INTO grupo (nombre_curso, etapa, num_alumnos) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            sentencia.setString(1, grupo.getNombreCurso());
            sentencia.setString(2, grupo.getEtapa());
            sentencia.setInt(3, grupo.getNumAlumnos());
            
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
    public Optional<Grupo> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM grupo WHERE id_grupo = ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    Grupo grupo = new Grupo(
                            rs.getInt("id_grupo"),
                            rs.getString("nombre_curso"),
                            rs.getString("etapa"),
                            rs.getInt("num_alumnos")
                    );
                    return Optional.of(grupo);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Grupo> findAll() throws SQLException {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT * FROM grupo";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet rs = sentencia.executeQuery()) {
            
            while (rs.next()) {
                Grupo grupo = new Grupo(
                        rs.getInt("id_grupo"),
                        rs.getString("nombre_curso"),
                        rs.getString("etapa"),
                        rs.getInt("num_alumnos")
                );
                grupos.add(grupo);
            }
        }
        return grupos;
    }

    @Override
    public boolean update(Grupo grupo) throws SQLException {
        String sql = "UPDATE grupo SET nombre_curso = ?, etapa = ?, num_alumnos = ? WHERE id_grupo = ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            
            sentencia.setString(1, grupo.getNombreCurso());
            sentencia.setString(2, grupo.getEtapa());
            sentencia.setInt(3, grupo.getNumAlumnos());
            sentencia.setInt(4, grupo.getIdGrupo());
            
            return sentencia.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM grupo WHERE id_grupo = ?";
        try (Connection conexion = ConexionBDD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            
            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        }
    }
}