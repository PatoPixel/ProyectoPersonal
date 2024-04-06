-- Acceso a MySQL: root / root
-- Materias -> pág. 223
-- Documentos -> pág. 208
-- Tipos Expediente -> pag 66 y varias mas
-- Estados -> Página 165
-- Actuaciones -> Página 36

SET sql_mode = 'STRICT_ALL_TABLES';

DROP DATABASE IF EXISTS elex;
CREATE DATABASE IF NOT EXISTS elex
CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

USE elex;

-- 1. Tabla Tipos Expediente
CREATE TABLE tipos_expediente
(
	tipos_id TINYINT NOT NULL UNIQUE AUTO_INCREMENT,
    materia VARCHAR (20) UNIQUE NOT NULL,
    activo TINYINT NOT NULL DEFAULT '1',
    PRIMARY KEY PK_tipos_expediente (tipos_id)
) 
COMMENT "Tabla Principal Tipos -> Expedientes";

-- 2. Tabla Expedientes
CREATE TABLE expedientes
(
	expediente_id INT NOT NULL UNIQUE AUTO_INCREMENT,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    fecha DATE NOT NULL,
    situacion ENUM('Pendiente','Enviado', 'En_Proceso', 'En_Espera_De_Juicio', 'Resuelto', 'Apelación', 'Cerrado') DEFAULT 'Pendiente',
    opciones VARCHAR(70) DEFAULT "",
    descripcion VARCHAR(255) NOT NULL,
    prioridad VARCHAR(20) NOT NULL,  -- (baja, media, alta, etc.).
    ubicacion VARCHAR(100) NOT NULL,
	tipo TINYINT NOT NULL,
    activo TINYINT NOT NULL DEFAULT '1',
    FOREIGN KEY (tipo) REFERENCES tipos_expediente (tipos_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY PK_expediente (expediente_id)
)
COMMENT "Tabla Principal Expedientes";

-- 3. Tabla Actuaciones
CREATE TABLE actuaciones
(
	actuaciones_id INT NOT NULL UNIQUE AUTO_INCREMENT,
    descripcion VARCHAR(255) NOT NULL,
    finalizado TINYINT DEFAULT 0,
    fecha DATE NOT NULL,
    modalidad VARCHAR(50) NOT NULL,  -- Con modalidad hago referencia a si es una reunión, llamada telefónica, envío de correo, etc
    expediente INT NOT NULL,
    activo TINYINT NOT NULL DEFAULT '1',
    FOREIGN KEY (expediente) REFERENCES expedientes (expediente_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY PK_actuaciones (actuaciones_id)
);

-- 4. Tabla Documentos
CREATE TABLE documentos
(
	documentos_id INT NOT NULL UNIQUE AUTO_INCREMENT,
	ruta VARCHAR(255) NOT NULL,
    tarifa DECIMAL(6,2) NOT NULL,
    categoria VARCHAR(50) NOT NULL,  -- (legal, financiero, técnico, etc.).
    expediente INT NOT NULL,
    activo TINYINT NOT NULL DEFAULT '1',
    FOREIGN KEY (expediente) REFERENCES expedientes (expediente_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY PK_documentos (documentos_id)
);

INSERT INTO tipos_expediente (materia)
VALUES ("Judicial"), ("Asistencia"), ("Informe"), ("Moción");

INSERT INTO expedientes
(codigo, fecha, situacion, opciones, descripcion,prioridad, ubicacion, tipo)
VALUES
("SEV-20240320-001", "2024-03-20", 'Pendiente', "Urgente, Confidencial", "Test1", "Alta", "Sevilla/Edificio Sevilla 1", 1),
("SEV-20240320-002", "2024-03-20", 'En_Espera_De_Juicio', "Urgente, Confidencial", "Test2", "Media", "Madrid/C. Principal", 2),
("SEV-20240320-003", "2024-03-20", 'Enviado', "Urgente", "Test3", "Baja", "Barcelona/casa", 1);

INSERT INTO actuaciones
(descripcion, finalizado, fecha, modalidad,expediente)
VALUES
("Actuacion1", 0, "2024-03-20", "reunion", 1),
("Actuación 2", 0, "2024-03-20", "meet", 2),
("Actuación 3", 1, "2024-03-21", "llamada telefónica", 3);


INSERT INTO documentos
(ruta, tarifa, categoria,expediente)
VALUES 
("static/pdfs/doc001d.pdf", 100.55, "Sport", 1),
("static/pdfs/doc002d.pdf", 90.55, "Futbil", 2),
("static/pdfs/doc003d.pdf", 80.55, "Financiera", 3);

SELECT * FROM tipos_expediente;
SELECT * FROM expedientes;
SELECT * FROM actuaciones;
SELECT * FROM documentos;

