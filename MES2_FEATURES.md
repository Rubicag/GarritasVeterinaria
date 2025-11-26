# 📧 Mes 2: Nuevas Funcionalidades - Guía Completa

## Resumen de Features Implementadas

✅ **Dashboard mejorado** - Métricas avanzadas con REST API  
✅ **Sistema de notificaciones** - Emails automáticos con Spring Scheduler  
✅ **Historial médico** - Upload de archivos y gestión completa

---

## 1. 📊 Dashboard Mejorado

### Endpoints Disponibles

#### 1.1 Estadísticas Generales
```http
GET /api/dashboard/stats
```

**Respuesta:**
```json
{
  "totalCitas": 45,
  "totalMascotas": 28,
  "totalServicios": 8,
  "totalUsuarios": 5
}
```

#### 1.2 Ingresos Mensuales
```http
GET /api/dashboard/ingresos-mensuales
```

**Respuesta:**
```json
{
  "meses": ["2025-01", "2025-02", "2025-03"],
  "ingresos": [15000.0, 18500.0, 21200.0],
  "total": 54700.0
}
```

#### 1.3 Top 5 Servicios Más Solicitados
```http
GET /api/dashboard/top-servicios
```

**Respuesta:**
```json
[
  {
    "servicioId": 1,
    "servicioNombre": "Consulta General",
    "cantidad": 25
  },
  {
    "servicioId": 3,
    "servicioNombre": "Vacunación",
    "cantidad": 18
  }
]
```

#### 1.4 Top 10 Mascotas Más Frecuentes
```http
GET /api/dashboard/mascotas-frecuentes
```

**Respuesta:**
```json
[
  {
    "mascotaId": 5,
    "mascotaNombre": "Firulais",
    "cantidadCitas": 12
  }
]
```

#### 1.5 Utilización de Veterinarios
```http
GET /api/dashboard/utilizacion-veterinarios
```

**Respuesta:**
```json
[
  {
    "veterinarioId": 2,
    "veterinarioNombre": "Dr. Carlos Pérez",
    "cantidadCitas": 35
  }
]
```

#### 1.6 Resumen Completo
```http
GET /api/dashboard/resumen
```

**Combina todas las métricas anteriores en un solo endpoint.**

### Tecnologías Utilizadas

- **Stream API**: Agregaciones y transformaciones
- **Collectors.groupingBy()**: Agrupamiento de datos
- **TreeMap**: Ordenamiento cronológico automático
- **YearMonth**: Manejo de periodos mensuales

---

## 2. 📧 Sistema de Notificaciones por Email

### Configuración

#### 2.1 Archivo `application.properties`

```properties
# Habilitar/deshabilitar envío de emails
app.email.enabled=false

# Configuración SMTP para Gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

#### 2.2 Obtener App Password de Gmail

1. Ir a https://myaccount.google.com/security
2. Activar "Verificación en dos pasos"
3. Ir a "Contraseñas de aplicaciones"
4. Generar nueva contraseña para "Correo"
5. Copiar la contraseña de 16 caracteres
6. Usar en `spring.mail.password`

#### 2.3 Para Desarrollo (Sin Gmail)

**Opción 1: MailHog (Recomendado)**
```bash
# Instalar MailHog
scoop install mailhog  # En Windows con Scoop
# O descargar de https://github.com/mailhog/MailHog/releases

# Ejecutar
mailhog

# Configurar en application.properties
spring.mail.host=localhost
spring.mail.port=1025
app.email.enabled=true
```

Ver emails en: http://localhost:8025

**Opción 2: Mailtrap**
- Registrarse en https://mailtrap.io (gratis)
- Obtener credenciales SMTP
- Configurar en `application.properties`

**Opción 3: Deshabilitar emails**
```properties
app.email.enabled=false
```
Los emails se logean en consola pero no se envían.

### Funcionalidades

#### 2.1 Email de Confirmación (Automático)

Se envía automáticamente al crear una cita:

```java
POST /api/citas
{
  "mascotaId": 1,
  "servicioId": 2,
  "fecha": "2025-11-20T10:00:00"
}
```

**Email enviado:**
```
Asunto: Confirmación de Cita - Veterinaria Garritas

Estimado/a propietario/a de Firulais,

Su cita ha sido registrada exitosamente:

Fecha y hora: 20/11/2025 10:00
Servicio: Consulta General

Recibirá un recordatorio 24 horas antes de su cita.

Atentamente,
Veterinaria Garritas
```

#### 2.2 Recordatorios Automáticos (Tarea Programada)

**Configuración:**
```java
@Scheduled(cron = "0 0 9 * * *") // Diario a las 9:00 AM
```

**Funcionamiento:**
1. Se ejecuta automáticamente cada día a las 9:00 AM
2. Busca citas en las próximas 24 horas
3. Envía email a cada propietario
4. Loguea éxitos y errores

**Personalizar horario:**
```java
// En NotificacionService.java
@Scheduled(cron = "0 0 8 * * *")  // 8:00 AM
@Scheduled(cron = "0 30 9 * * *") // 9:30 AM
@Scheduled(cron = "0 0 18 * * ?") // 6:00 PM
```

### Arquitectura

```
CitaController (POST /api/citas)
    ↓
CitaService.create()
    ↓
citaRepository.save()
    ↓
NotificacionService.enviarConfirmacionCita()
    ↓
EmailService.enviarEmail()
    ↓
JavaMailSender.send()
```

**Resiliencia:** Si falla el email, la transacción de la cita NO se revierte.

---

## 3. 🏥 Historial Médico Completo

### Modelo de Datos

```java
HistorialMedico {
  id: Long
  cita: Cita              // Relación con cita
  diagnostico: String     // Obligatorio (max 500 chars)
  tratamiento: String     // Texto largo (TEXT)
  notasMedicas: String    // Texto largo (TEXT)
  fechaRegistro: LocalDateTime // Auto-generado
  
  // Archivo adjunto
  archivo: byte[]         // Blob (radiografía, análisis, etc.)
  nombreArchivo: String   // Nombre original del archivo
  tipoArchivo: String     // MIME type (image/jpeg, application/pdf)
  tamanioArchivo: Long    // Tamaño en bytes
}
```

### Endpoints REST

#### 3.1 Listar Todos los Historiales
```http
GET /api/historial-medico
```

#### 3.2 Obtener Historial por ID
```http
GET /api/historial-medico/{id}
```

#### 3.3 Historial de una Cita
```http
GET /api/historial-medico/cita/{citaId}
```

#### 3.4 Historial Completo de una Mascota
```http
GET /api/historial-medico/mascota/{mascotaId}
```

**Ordenado por fecha DESC (más reciente primero).**

#### 3.5 Crear Registro con Archivo

**cURL:**
```bash
curl -X POST http://localhost:8080/api/historial-medico \
  -F "citaId=1" \
  -F "diagnostico=Fractura de fémur izquierdo" \
  -F "tratamiento=Cirugía reconstructiva + reposo 30 días" \
  -F "notasMedicas=Pronóstico favorable, control en 2 semanas" \
  -F "archivo=@radiografia.jpg"
```

**Postman / Swagger:**
1. Seleccionar `POST /api/historial-medico`
2. Body → form-data
3. Agregar campos:
   - `citaId`: 1
   - `diagnostico`: Fractura de fémur
   - `tratamiento`: Cirugía + reposo
   - `archivo`: [Seleccionar archivo]

**JavaScript (Fetch API):**
```javascript
const formData = new FormData();
formData.append('citaId', 1);
formData.append('diagnostico', 'Fractura de fémur');
formData.append('tratamiento', 'Cirugía + reposo 30 días');
formData.append('archivo', fileInput.files[0]);

fetch('/api/historial-medico', {
  method: 'POST',
  body: formData
})
.then(res => res.json())
.then(data => console.log(data));
```

#### 3.6 Descargar Archivo Adjunto
```http
GET /api/historial-medico/{id}/archivo
```

**Respuesta:**
- Content-Type: `image/jpeg` o `application/pdf`
- Content-Disposition: `attachment; filename="radiografia.jpg"`
- Body: Bytes del archivo

**Uso en frontend:**
```html
<a href="/api/historial-medico/1/archivo" download>
  Descargar Radiografía
</a>
```

#### 3.7 Actualizar Registro
```http
PUT /api/historial-medico/{id}
```

Acepta los mismos parámetros que POST (todos opcionales).

#### 3.8 Eliminar Registro
```http
DELETE /api/historial-medico/{id}
```

### Límites de Archivos

**Configurado en `application.properties`:**
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

**Tipos de archivo soportados:**
- Imágenes: JPG, PNG, GIF
- Documentos: PDF, DOCX
- Otros: Cualquier tipo MIME

### Casos de Uso

#### Caso 1: Registro de Consulta con Radiografía

1. Veterinario atiende cita (ID: 42)
2. Diagnostica fractura
3. Toma radiografía
4. Crea historial médico con radiografía adjunta

```bash
curl -X POST /api/historial-medico \
  -F "citaId=42" \
  -F "diagnostico=Fractura de radio" \
  -F "tratamiento=Inmovilización 3 semanas" \
  -F "archivo=@rx_radio.jpg"
```

#### Caso 2: Ver Historial Completo de Mascota

```javascript
// Frontend: Obtener historial de mascota ID=5
fetch('/api/historial-medico/mascota/5')
  .then(res => res.json())
  .then(historiales => {
    historiales.forEach(h => {
      console.log(`${h.fechaRegistro}: ${h.diagnostico}`);
      if (h.nombreArchivo) {
        console.log(`Archivo adjunto: ${h.nombreArchivo}`);
      }
    });
  });
```

#### Caso 3: Actualizar Notas Médicas

```bash
curl -X PUT /api/historial-medico/1 \
  -F "notasMedicas=Evolución favorable, retirar vendaje"
```

---

## 🧪 Pruebas con Swagger

1. **Iniciar aplicación:**
   ```bash
   .\mvnw.cmd spring-boot:run
   ```

2. **Abrir Swagger UI:**
   http://localhost:8080/swagger-ui.html

3. **Probar Dashboard:**
   - Expandir "Dashboard"
   - Probar `GET /api/dashboard/stats`
   - Probar `GET /api/dashboard/resumen`

4. **Probar Historial Médico:**
   - Expandir "Historial Médico"
   - Probar `POST /api/historial-medico` (con archivo)
   - Copiar el `id` de la respuesta
   - Probar `GET /api/historial-medico/{id}/archivo`

---

## 📝 Checklist de Activación

### Para Activar Emails en Producción

- [ ] Crear cuenta Gmail o SMTP
- [ ] Generar App Password
- [ ] Configurar `spring.mail.*` en `application.properties`
- [ ] Configurar `app.email.enabled=true`
- [ ] Reiniciar aplicación
- [ ] Crear cita de prueba
- [ ] Verificar email de confirmación
- [ ] Esperar siguiente día 9:00 AM para recordatorios

### Para Usar Historial Médico

- [ ] Ejecutar schema SQL (crear tabla `historial_medico`)
- [ ] Crear cita existente
- [ ] Probar POST con archivo desde Swagger
- [ ] Verificar descarga de archivo
- [ ] Probar GET por mascota

---

## 🎉 Resumen

**13/13 tareas completadas (100%)**

- ✅ Semanas 1-2: Seguridad (GlobalExceptionHandler, Validaciones, BCrypt)
- ✅ Semanas 3-4: UX (Selector, Filtros, Paginación)
- ✅ Semanas 5-6: APIs (DTOs, Swagger, Tests)
- ✅ Mes 2: Features avanzadas (Dashboard, Emails, Historial)

**El sistema está production-ready** con:
- API REST completa y documentada
- Notificaciones automáticas
- Gestión de archivos médicos
- Métricas de negocio
- Cobertura de tests
- Seguridad implementada

---

**Última actualización:** 18 de noviembre de 2025
