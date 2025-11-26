-- schema.sql para H2 compatible

CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    dni VARCHAR(8) NOT NULL,
    correo VARCHAR(255) NOT NULL UNIQUE,
    telefono VARCHAR(255),
    direccion VARCHAR(255),
    usuario VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

CREATE TABLE mascota (
    id_mascota INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    especie VARCHAR(255) NOT NULL,
    raza VARCHAR(255),
    sexo VARCHAR(10) NOT NULL,
    fecha_nacimiento DATE,
    peso DECIMAL(5,2),
    id_usuario INT NOT NULL,
    edad INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE servicio (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255),
    precio DOUBLE
);

CREATE TABLE cita (
    id_cita BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP,
    hora TIME,
    estado VARCHAR(20) DEFAULT 'Pendiente',
    id_mascota INT NOT NULL,
    id_servicio INT NOT NULL,
    id_veterinario INT NOT NULL,
    mascota_id BIGINT,
    observaciones VARCHAR(255),
    servicio_id BIGINT,
    FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota),
    FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio),
    FOREIGN KEY (id_veterinario) REFERENCES usuario(id_usuario)
);

CREATE TABLE historial_medico (
    id_historial BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cita BIGINT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_consulta TIMESTAMP,
    tipo_consulta VARCHAR(20) DEFAULT 'CONSULTA',
    motivo_consulta VARCHAR(500),
    diagnostico VARCHAR(500) NOT NULL,
    tratamiento VARCHAR(1000),
    medicamentos VARCHAR(500),
    observaciones VARCHAR(1000),
    estado VARCHAR(20) DEFAULT 'COMPLETADO',
    costo DECIMAL(10,2) DEFAULT 0.00,
    archivo BLOB,
    nombre_archivo VARCHAR(255),
    tipo_archivo VARCHAR(100),
    tamanio_archivo BIGINT,
    FOREIGN KEY (id_cita) REFERENCES cita(id_cita)
);

CREATE TABLE producto (
    id_producto BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255),
    categoria VARCHAR(50) DEFAULT 'OTRO',
    precio DOUBLE,
    stock INT DEFAULT 0,
    proveedor VARCHAR(100),
    fecha_vencimiento DATE,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE movimiento_inventario (
    id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_producto BIGINT NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    cantidad INT NOT NULL,
    stock_anterior INT NOT NULL,
    stock_actual INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    motivo VARCHAR(255),
    usuario VARCHAR(100),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE TABLE reporte (
    id_reporte BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255),
    descripcion VARCHAR(255),
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL,
    fecha TIMESTAMP,
    mascota_id BIGINT,
    usuario_id BIGINT,
    contenido VARCHAR(255),
    id_usuario_generador BIGINT,
    tipo_reporte VARCHAR(255),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE configuracion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(255) NOT NULL UNIQUE,
    valor VARCHAR(2000)
);
