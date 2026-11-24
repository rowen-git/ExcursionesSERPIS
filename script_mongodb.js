db = db.getSiblingDB("excursiones_serpis");
db.resenas.drop();
db.createCollection("resenas");
db.resenas.createIndex({ idExcursion: 1 }, { unique: true });

db.resenas.insertMany([
  {
    idExcursion: NumberInt(1), // Museo de las Ciencias
    fechaCreacion: new Date("2026-10-16T16:30:00Z"),
    valoracionGlobal: NumberInt(5),
    comentarioGeneral: "Excursión muy bien organizada. El alumnado participó activamente.",
    actividades: [
      { nombre: "Visita guiada por el museo", valoracion: NumberInt(5) },
      { nombre: "Taller práctico de ciencia", valoracion: NumberInt(4) }
    ],
    incidencias: [
      "Retraso de 10 minutos del autobús en la recogida de vuelta."
    ]
  },
  {
    idExcursion: NumberInt(2), // Ruta Senderismo
    fechaCreacion: new Date("2026-10-12T18:15:00Z"),
    valoracionGlobal: NumberInt(3),
    comentarioGeneral: "Ruta interesante, pero la charla final se hizo pesada.",
    actividades: [
      { nombre: "Senderismo por el parque natural", valoracion: NumberInt(4) },
      { nombre: "Charla ambiental en mirador", valoracion: NumberInt(2) }
    ],
    incidencias: [
      "Lluvia moderada durante el tramo final de la ruta.",
      "Un alumno olvidó la autorización y tuvo que traerla un familiar."
    ]
  },
  {
    idExcursion: NumberInt(4), // Bioparc Valencia
    fechaCreacion: new Date("2026-10-29T14:00:00Z"),
    valoracionGlobal: NumberInt(4),
    comentarioGeneral: "Jornada muy positiva y sin contratiempos.",
    actividades: [
      { nombre: "Recorrido por la sabana africana", valoracion: NumberInt(5) },
      { nombre: "Exhibición educativa de aves", valoracion: NumberInt(4) }
    ],
    incidencias: []
  }
]);

print("Índices creados:");
printjson(db.resenas.getIndexes());
print("Total de reseñas insertadas: " + db.resenas.countDocuments());