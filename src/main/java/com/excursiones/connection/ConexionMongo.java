package com.excursiones.connection;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public final class ConexionMongo {

    private static final String URI = "mongodb://localhost:27017";
    private static final String BASE_DATOS = "excursiones_serpis";
    private static final String COLECCION_RESENAS = "resenas";

    private static MongoClient cliente;

    private ConexionMongo() {
    }

    public static boolean conectar() {
        try {
            cliente = MongoClients.create(URI);
            cliente.getDatabase(BASE_DATOS).runCommand(new Document("ping", 1));
            return true;
        } catch (MongoException e) {
            System.err.println("Error al conectar con MongoDB: " + e.getMessage());
            cerrar();
            return false;
        }
    }

    public static MongoCollection<Document> obtenerColeccionResenas() {
        if (cliente == null) {
            conectar();
        }
        return cliente.getDatabase(BASE_DATOS).getCollection(COLECCION_RESENAS);
    }

    public static void cerrar() {
        if (cliente != null) {
            cliente.close();
            cliente = null;
        }
    }
}