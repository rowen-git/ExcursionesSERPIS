package com.excursiones.connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBDD {
    private static final String URL = "jdbc:mysql://localhost:3306/excursiones_serpis";
    private static final String USUARIO = "serpis";
    private static final String CONTRASENA = "serpis";

    public static java.sql.Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}