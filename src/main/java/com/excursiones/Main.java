package com.excursiones;

import com.excursiones.connection.ConexionMongo;
import com.excursiones.view.MenuView;

public class Main {
    public static void main(String[] args) {
        if (ConexionMongo.conectar()) { //if (ConexionMongo.conectar() == true)
            MenuView vista = new MenuView();
            vista.mostrarMenu();
            ConexionMongo.cerrar();
        }
    }
}