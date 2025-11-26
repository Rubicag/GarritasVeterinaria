# Historial Médico - Guía de Uso

## ✅ Tabla creada exitosamente

La tabla `historial_medico` ha sido creada en la base de datos `garritas_veterinaria` con la siguiente estructura:

### Estructura de la tabla

```sql
CREATE TABLE historial_medico (
    id_historial BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cita BIGINT NOT NULL,                -- FK a cita(id_cita)
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_consulta DATETIME,
    tipo_consulta ENUM('CONSULTA', 'VACUNACION', 'CIRUGIA', 'EMERGENCIA', 'CONTROL'),
    motivo_consulta VARCHAR(500),
    diagnostico VARCHAR(500) NOT NULL,
    tratamiento TEXT,
    medicamentos VARCHAR(500),
    observaciones TEXT,
    estado ENUM('COMPLETADO', 'PENDIENTE', 'EN_PROGRESO'),
    costo DECIMAL(10, 2) DEFAULT 0.00,
    archivo LONGBLOB,                       -- Archivos adjuntos (radiografías, etc.)
    nombre_archivo VARCHAR(255),
    tipo_archivo VARCHAR(100),
    tamanio_archivo BIGINT
);
```

### Datos de ejemplo

Se insertaron **10 registros de ejemplo** basados en las citas existentes (IDs 17-26):
- 4 consultas generales
- 2 vacunaciones
- 1 cirugía
- 1 emergencia
- 1 control
- 1 consulta con radiografía

## API REST disponible

### Endpoints principales

#### 1. Listar todos los historiales
```http
GET /api/historial-medico
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "cita": { "id": 17, "fecha": "2025-10-05T10:00:00" },
    "fechaRegistro": "2025-10-05T10:30:00",
    "tipoConsulta": "CONSULTA",
    "diagnostico": "Mascota en buen estado general de salud",
    "tratamiento": "Continuar con dieta actual y ejercicio regular",
    "estado": "COMPLETADO",
    "costo": 50.00
  }
]
```

#### 2. Obtener historial por ID
```http
GET /api/historial-medico/{id}
```

#### 3. Obtener historial de una mascota específica
```http
GET /api/historial-medico/mascota/{mascotaId}
```

**Nota:** Este endpoint usa la relación `cita -> mascota` para obtener todos los historiales de una mascota.

#### 4. Crear nuevo historial médico
```http
POST /api/historial-medico
Content-Type: multipart/form-data
```

**Parámetros:**
- `citaId` (required): ID de la cita
- `diagnostico` (required): Diagnóstico
- `tratamiento` (optional): Tratamiento prescrito
- `observaciones` (optional): Notas adicionales
- `archivo` (optional): Archivo adjunto (imagen, PDF, etc.)

**Ejemplo con cURL:**
```bash
curl -X POST http://localhost:8080/api/historial-medico \
  -F "citaId=1" \
  -F "diagnostico=Gastroenteritis leve" \
  -F "tratamiento=Dieta blanda 3 días" \
  -F "archivo=@radiografia.jpg"
```

#### 5. Actualizar historial
```http
PUT /api/historial-medico/{id}
Content-Type: multipart/form-data
```

#### 6. Descargar archivo adjunto
```http
GET /api/historial-medico/{id}/archivo
```

#### 7. Eliminar historial
```http
DELETE /api/historial-medico/{id}
```

## Consultas SQL útiles

### Ver historiales con información completa
```sql
SELECT 
    h.id_historial,
    h.fecha_registro,
    h.tipo_consulta,
    h.diagnostico,
    h.estado,
    h.costo,
    c.fecha as fecha_cita,
    m.nombre as mascota,
    m.especie,
    u.nombre as veterinario,
    s.nombre as servicio
FROM historial_medico h
INNER JOIN cita c ON h.id_cita = c.id_cita
INNER JOIN mascota m ON c.id_mascota = m.id_mascota
INNER JOIN usuario u ON c.id_veterinario = u.id_usuario
INNER JOIN servicio s ON c.id_servicio = s.id_servicio
ORDER BY h.fecha_registro DESC;
```

### Historial completo de una mascota
```sql
SELECT 
    h.id_historial,
    h.fecha_registro,
    h.tipo_consulta,
    h.diagnostico,
    h.tratamiento,
    h.medicamentos,
    h.costo,
    c.fecha as fecha_cita,
    u.nombre as veterinario
FROM historial_medico h
INNER JOIN cita c ON h.id_cita = c.id_cita
INNER JOIN usuario u ON c.id_veterinario = u.id_usuario
WHERE c.id_mascota = 1  -- Cambiar por ID de mascota
ORDER BY h.fecha_registro DESC;
```

### Estadísticas por tipo de consulta
```sql
SELECT 
    tipo_consulta,
    COUNT(*) as total_consultas,
    SUM(costo) as ingreso_total,
    AVG(costo) as costo_promedio
FROM historial_medico
WHERE estado = 'COMPLETADO'
GROUP BY tipo_consulta
ORDER BY total_consultas DESC;
```

## Modelo Java

La entidad `HistorialMedico.java` mapea a esta tabla:

```java
@Entity
@Table(name = "historial_medico")
public class HistorialMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;  // Relación con Cita

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consulta")
    private TipoConsulta tipoConsulta;  // CONSULTA, VACUNACION, CIRUGIA, etc.

    @Column(name = "diagnostico", length = 500, nullable = false)
    private String diagnostico;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;  // COMPLETADO, PENDIENTE, EN_PROGRESO

    @Column(name = "costo", precision = 10, scale = 2)
    private BigDecimal costo;

    // Archivos adjuntos
    @Lob
    private byte[] archivo;
    private String nombreArchivo;
    private String tipoArchivo;
    private Long tamanioArchivo;
}
```

## Próximos pasos

1. ✅ Tabla creada en MySQL
2. ✅ Entidad Java configurada
3. ✅ Repositorio y servicio implementados
4. ✅ API REST documentada con Swagger
5. ⏳ Pruebas de integración
6. ⏳ Interfaz web (Thymeleaf templates)

## Testing

Para probar la API, puedes usar:

1. **Swagger UI**: http://localhost:8080/swagger-ui.html
2. **Postman/Insomnia**: Importar colección de endpoints
3. **cURL**: Comandos desde terminal

## Notas importantes

- **Relación con Cita**: Cada historial médico está asociado a UNA cita
- **Archivos adjuntos**: Máximo 10MB por archivo (configurado en `application.properties`)
- **Enums**: `TipoConsulta` y `Estado` son enumeraciones definidas en Java
- **Cascada**: Al eliminar una cita, se eliminan automáticamente sus historiales médicos
- **Email deshabilitado por defecto**: Configurar SMTP en `application.properties` si deseas enviar notificaciones

## Soporte

Para dudas o problemas:
1. Revisar logs en `logs/application.log`
2. Verificar errores de compilación: `.\mvnw.cmd clean compile`
3. Ejecutar tests: `.\mvnw.cmd test`
