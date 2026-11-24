package com.excursiones.dao;

import com.excursiones.connection.ConexionMongo;
import com.excursiones.model.Actividad;
import com.excursiones.model.Resena;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.pull;
import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;

public class ResenaDao {

    private final MongoCollection<Document> resenas;

    public ResenaDao() {
        this.resenas = ConexionMongo.obtenerColeccionResenas();
        crearIndiceUnico();
    }

    private void crearIndiceUnico() {
        IndexOptions opciones = new IndexOptions().unique(true);
        resenas.createIndex(Indexes.ascending("idExcursion"), opciones);
    }

    public ObjectId insertar(Resena resena) {
        InsertOneResult resultado = resenas.insertOne(aDocument(resena));
        return resultado.getInsertedId().asObjectId().getValue();
    }

    public Optional<Resena> buscarPorIdExcursion(int idExcursion) {
        Document documento = resenas.find(eq("idExcursion", idExcursion)).first();
        if (documento == null) {
            return Optional.empty();
        }
        return Optional.of(aModel(documento));
    }

    public boolean existePorIdExcursion(int idExcursion) {
        return resenas.countDocuments(eq("idExcursion", idExcursion)) > 0;
    }

    public boolean actualizarDatosGenerales(int idExcursion, int valoracionGlobal, String comentarioGeneral) {
        Bson cambios = combine(
                set("valoracionGlobal", valoracionGlobal),
                set("comentarioGeneral", comentarioGeneral)
        );
        UpdateResult resultado = resenas.updateOne(eq("idExcursion", idExcursion), cambios);
        return resultado.getMatchedCount() == 1;
    }

    public boolean agregarActividad(int idExcursion, Actividad actividad) {
        UpdateResult resultado = resenas.updateOne(
                eq("idExcursion", idExcursion),
                push("actividades", aDocumentActividad(actividad))
        );
        return resultado.getMatchedCount() == 1;
    }

    public boolean modificarActividad(int idExcursion, int indice, Actividad actividad) {
        Bson cambios = combine(
                set("actividades." + indice + ".nombre", actividad.getNombre()),
                set("actividades." + indice + ".valoracion", actividad.getValoracion())
        );
        UpdateResult resultado = resenas.updateOne(eq("idExcursion", idExcursion), cambios);
        return resultado.getMatchedCount() == 1;
    }

    public boolean eliminarActividad(int idExcursion, int indice) {
        UpdateResult resUnset = resenas.updateOne(
                eq("idExcursion", idExcursion),
                unset("actividades." + indice)
        );
        if (resUnset.getMatchedCount() != 1) {
            return false;
        }
        resenas.updateOne(eq("idExcursion", idExcursion), pull("actividades", null));
        return true;
    }

    public boolean agregarIncidencia(int idExcursion, String incidencia) {
        UpdateResult resultado = resenas.updateOne(
                eq("idExcursion", idExcursion),
                push("incidencias", incidencia)
        );
        return resultado.getMatchedCount() == 1;
    }

    public boolean modificarIncidencia(int idExcursion, int indice, String nuevaIncidencia) {
        UpdateResult resultado = resenas.updateOne(
                eq("idExcursion", idExcursion),
                set("incidencias." + indice, nuevaIncidencia)
        );
        return resultado.getMatchedCount() == 1;
    }

    public boolean eliminarIncidencia(int idExcursion, int indice) {
        UpdateResult resUnset = resenas.updateOne(
                eq("idExcursion", idExcursion),
                unset("incidencias." + indice)
        );
        if (resUnset.getMatchedCount() != 1) {
            return false;
        }
        resenas.updateOne(eq("idExcursion", idExcursion), pull("incidencias", null));
        return true;
    }

    public boolean eliminar(int idExcursion) {
        return resenas.deleteOne(eq("idExcursion", idExcursion)).getDeletedCount() == 1;
    }

    public List<Resena> listarTodasOrdenadasPorFechaDesc() {
        return aLista(resenas.find().sort(Sorts.descending("fechaCreacion")));
    }

    public List<Resena> buscarPorValoracionGlobal(int valoracion) {
        Bson filtro = eq("valoracionGlobal", valoracion);
        return aLista(resenas.find(filtro).sort(Sorts.descending("fechaCreacion")));
    }

    public List<Resena> buscarConActividadValoracionInferiorA(int valoracionLimite) {
        Bson filtro = lt("actividades.valoracion", valoracionLimite);
        return aLista(resenas.find(filtro).sort(Sorts.descending("fechaCreacion")));
    }

    public List<Resena> listarConIncidencias() {
        Bson filtro = exists("incidencias.0", true);
        return aLista(resenas.find(filtro).sort(Sorts.descending("fechaCreacion")));
    }

    private List<Resena> aLista(Iterable<Document> documentos) {
        List<Resena> resultado = new ArrayList<>();
        for (Document doc : documentos) {
            resultado.add(aModel(doc));
        }
        return resultado;
    }

    private Document aDocument(Resena resena) {
        Date fechaDate = Date.from(resena.getFechaCreacion().atZone(ZoneId.systemDefault()).toInstant());
        return new Document("idExcursion", resena.getIdExcursion())
                .append("fechaCreacion", fechaDate)
                .append("valoracionGlobal", resena.getValoracionGlobal())
                .append("comentarioGeneral", resena.getComentarioGeneral())
                .append("actividades", documentsDeActividades(resena.getActividades()))
                .append("incidencias", resena.getIncidencias());
    }

    private List<Document> documentsDeActividades(List<Actividad> actividades) {
        List<Document> documentos = new ArrayList<>();
        for (Actividad actividad : actividades) {
            documentos.add(aDocumentActividad(actividad));
        }
        return documentos;
    }

    private Document aDocumentActividad(Actividad actividad) {
        return new Document("nombre", actividad.getNombre())
                .append("valoracion", actividad.getValoracion());
    }

    private Resena aModel(Document doc) {
        Resena resena = new Resena();
        resena.setId(doc.getObjectId("_id"));
        resena.setIdExcursion(leerEntero(doc, "idExcursion"));

        Date fechaDoc = doc.getDate("fechaCreacion");
        if (fechaDoc != null) {
            LocalDateTime fecha = Instant.ofEpochMilli(fechaDoc.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            resena.setFechaCreacion(fecha);
        }

        resena.setValoracionGlobal(leerEntero(doc, "valoracionGlobal"));
        resena.setComentarioGeneral(doc.getString("comentarioGeneral"));

        List<Actividad> actividades = new ArrayList<>();
        for (Document actDoc : doc.getList("actividades", Document.class, List.of())) {
            actividades.add(new Actividad(
                    actDoc.getString("nombre"),
                    leerEntero(actDoc, "valoracion")
            ));
        }
        resena.setActividades(actividades);

        resena.setIncidencias(new ArrayList<>(
                doc.getList("incidencias", String.class, List.of())
        ));

        return resena;
    }

    private int leerEntero(Document doc, String campo) {
        Number numero = doc.get(campo, Number.class);
        return numero != null ? numero.intValue() : 0;
    }
}