DROP DATABASE IF EXISTS excursiones_serpis; 
CREATE DATABASE excursiones_serpis;
USE excursiones_serpis;

-- Tabla Grupo
CREATE TABLE grupo (
    id_grupo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_curso VARCHAR(50) NOT NULL,
    etapa VARCHAR(50) NOT NULL,
    num_alumnos INT NOT NULL
);

-- Tabla Profesor
CREATE TABLE profesor (
    id_profesor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    anio_nacimiento INT NOT NULL,
    telefono VARCHAR(20) NOT NULL
);

-- Tabla Excursión
CREATE TABLE excursion (
    id_excursion INT AUTO_INCREMENT PRIMARY KEY,
    destino VARCHAR(100) NOT NULL,
    duracion_horas INT NOT NULL, 
    fecha DATE NOT NULL,
    precio DECIMAL(6,2) NOT NULL,
    finalizada BOOLEAN NOT NULL DEFAULT FALSE,
    id_grupo INT NOT NULL,
    
    -- Clave Foránea
    CONSTRAINT fk_excursion_grupo 
        FOREIGN KEY (id_grupo) 
        REFERENCES grupo(id_grupo)
);

-- Tabla intermedia entre Excursión y Profesor
CREATE TABLE excursion_profesor (
    id_excursion INT NOT NULL,
    id_profesor INT NOT NULL,
    
    PRIMARY KEY (id_excursion, id_profesor),
    
    -- Claves foránea
    CONSTRAINT fk_ep_excursion 
        FOREIGN KEY (id_excursion) REFERENCES excursion(id_excursion),
    CONSTRAINT fk_ep_profesor 
        FOREIGN KEY (id_profesor) REFERENCES profesor(id_profesor)
);


-- DATOS PARA PRUEBA
INSERT INTO grupo (nombre_curso, etapa, num_alumnos) VALUES 
('1º DAW', 'FP', 30),
('4º ESO A', 'ESO', 25),
('2º Ciencias', 'Bachillerato', 28);

INSERT INTO profesor (nombre, apellidos, anio_nacimiento, telefono) VALUES 
('Carlos', 'García López', 1990, '111222333'),
('María', 'Martínez Ruiz', 1985, '444555666'),
('Alejandro', 'Fernández Gómez', 1995, '777888999');

INSERT INTO excursion (destino, duracion_horas, fecha, precio, finalizada, id_grupo) VALUES 
('Museo de las Ciencias', 4, '2026-10-15', 12.50, TRUE, 1),   
('Ruta Senderismo', 6, '2026-10-10', 5.00, TRUE, 2),          
('Feria IT', 5, '2026-12-05', 0.00, FALSE, 1),                
('Bioparc Valencia', 5, '2026-10-28', 15.00, TRUE, 3),        
('Ciudad de las Artes', 4, '2026-11-05', 10.00, TRUE, 2);     

INSERT INTO excursion_profesor (id_excursion, id_profesor) VALUES 
(1, 1), (1, 2),
(2, 3),
(3, 1), (3, 3),
(4, 2),
(5, 1);