package com.mycompany.controller;

import com.mycompany.model.Usuario;
import com.mycompany.service.UsuarioService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clinica")
@CrossOrigin(origins = "*")
public class ClinicaController {

    private final UsuarioService usuarioService;

    public ClinicaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/informacion")
    public ResponseEntity<?> getInformacionClinica() {
        try {
            Map<String, Object> info = new HashMap<>();
            info.put("nombre", "Garritas Veterinaria");
            info.put("direccion", "Av. Principal 123, Lima, Perú");
            info.put("telefono", "+51 1 234-5678");
            info.put("email", "info@garritasveterinaria.com");
            info.put("horarioAtencion", "Lunes a Viernes: 8:00 AM - 6:00 PM, Sábados: 9:00 AM - 2:00 PM");
            info.put("emergencias24h", true);
            info.put("telefonoEmergencias", "+51 1 999-8888");
            
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener información de la clínica"));
        }
    }

    @PutMapping("/informacion")
    public ResponseEntity<?> actualizarInformacionClinica(@RequestBody Map<String, String> info) {
        try {
            // En una implementación real, esto se guardaría en la base de datos
            // Por ahora, simulamos la actualización
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("message", "Información de la clínica actualizada correctamente");
            respuesta.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            respuesta.put("datosActualizados", info);
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar información de la clínica"));
        }
    }

    @GetMapping("/veterinarios")
    public ResponseEntity<?> getVeterinarios() {
        try {
            // Obtener usuarios con rol de veterinario
            List<Usuario> veterinarios = usuarioService.findAll().stream()
                    .filter(u -> u.getRol() != null &&
                               (u.getRol().getNombre().equalsIgnoreCase("VETERINARIO") ||
                                u.getRol().getNombre().equalsIgnoreCase("ADMIN")))
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> veterinariosInfo = veterinarios.stream()
                    .map(vet -> {
                        Map<String, Object> info = new HashMap<>();
                        info.put("id", vet.getId());
                        info.put("nombre", vet.getUsuario());
                        info.put("email", vet.getCorreo());
                        info.put("rol", vet.getRol().getNombre());
                        info.put("fechaCreacion", null); // La entidad Usuario no tiene fecha de creación
                        // No incluimos la contraseña por seguridad
                        return info;
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "veterinarios", veterinariosInfo,
                "total", veterinariosInfo.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener lista de veterinarios"));
        }
    }

    @GetMapping("/horarios")
    public ResponseEntity<?> getHorarios() {
        try {
            Map<String, Object> horarios = new HashMap<>();
            
            // Horarios de atención general
            Map<String, String> horariosGenerales = new HashMap<>();
            horariosGenerales.put("lunes", "8:00 AM - 6:00 PM");
            horariosGenerales.put("martes", "8:00 AM - 6:00 PM");
            horariosGenerales.put("miercoles", "8:00 AM - 6:00 PM");
            horariosGenerales.put("jueves", "8:00 AM - 6:00 PM");
            horariosGenerales.put("viernes", "8:00 AM - 6:00 PM");
            horariosGenerales.put("sabado", "9:00 AM - 2:00 PM");
            horariosGenerales.put("domingo", "Cerrado");
            
            horarios.put("horariosGenerales", horariosGenerales);
            
            // Horarios de emergencia
            horarios.put("emergencias", "24 horas - 7 días de la semana");
            horarios.put("telefonoEmergencias", "+51 1 999-8888");
            
            // Servicios especiales
            Map<String, String> serviciosEspeciales = new HashMap<>();
            serviciosEspeciales.put("cirugia", "Lunes a Viernes: 9:00 AM - 4:00 PM");
            serviciosEspeciales.put("estetica", "Martes y Jueves: 10:00 AM - 5:00 PM");
            serviciosEspeciales.put("laboratorio", "Lunes a Viernes: 8:00 AM - 5:00 PM");
            
            horarios.put("serviciosEspeciales", serviciosEspeciales);
            
            return ResponseEntity.ok(horarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener horarios"));
        }
    }

    @PutMapping("/horarios")
    public ResponseEntity<?> actualizarHorarios(@RequestBody Map<String, Object> horarios) {
        try {
            // En una implementación real, esto se guardaría en la base de datos
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("message", "Horarios actualizados correctamente");
            respuesta.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            respuesta.put("horariosActualizados", horarios);
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar horarios"));
        }
    }

    @GetMapping("/servicios-disponibles")
    public ResponseEntity<?> getServiciosDisponibles() {
        try {
            Map<String, Object> servicios = new HashMap<>();
            
            // Servicios médicos
            List<String> serviciosMedicos = List.of(
                "Consulta general",
                "Vacunación",
                "Desparasitación",
                "Esterilización",
                "Cirugía general",
                "Odontología veterinaria",
                "Oftalmología",
                "Cardiología"
            );
            
            // Servicios de diagnóstico
            List<String> serviciosDiagnostico = List.of(
                "Radiografías",
                "Ecografías",
                "Análisis de sangre",
                "Análisis de orina",
                "Biopsias",
                "Electrocardiograma"
            );
            
            // Servicios de estética
            List<String> serviciosEstetica = List.of(
                "Baño medicinal",
                "Corte de pelo",
                "Corte de uñas",
                "Limpieza de oídos",
                "Tratamiento antipulgas"
            );
            
            // Servicios de emergencia
            List<String> serviciosEmergencia = List.of(
                "Atención 24h",
                "Hospitalización",
                "Cuidados intensivos",
                "Cirugía de emergencia"
            );
            
            servicios.put("medicos", serviciosMedicos);
            servicios.put("diagnostico", serviciosDiagnostico);
            servicios.put("estetica", serviciosEstetica);
            servicios.put("emergencia", serviciosEmergencia);
            
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener servicios disponibles"));
        }
    }

    @GetMapping("/contacto")
    public ResponseEntity<?> getContacto() {
        try {
            Map<String, Object> contacto = new HashMap<>();
            contacto.put("telefono", "+51 1 234-5678");
            contacto.put("celular", "+51 999 123-456");
            contacto.put("email", "info@garritasveterinaria.com");
            contacto.put("emergencias", "+51 1 999-8888");
            contacto.put("whatsapp", "+51 999 123-456");
            contacto.put("facebook", "@GarritasVeterinaria");
            contacto.put("instagram", "@garritas_veterinaria");
            
            Map<String, String> direccion = new HashMap<>();
            direccion.put("calle", "Av. Principal 123");
            direccion.put("distrito", "Lima Centro");
            direccion.put("ciudad", "Lima");
            direccion.put("pais", "Perú");
            direccion.put("codigoPostal", "15001");
            
            contacto.put("direccion", direccion);
            
            return ResponseEntity.ok(contacto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener información de contacto"));
        }
    }

    @PostMapping("/mensaje")
    public ResponseEntity<?> enviarMensaje(@RequestBody Map<String, String> mensaje) {
        try {
            // En una implementación real, aquí se enviaría el mensaje por email
            // o se guardaría en una base de datos para seguimiento
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("message", "Mensaje enviado correctamente. Le responderemos a la brevedad.");
            respuesta.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            respuesta.put("numeroTicket", "TICKET-" + System.currentTimeMillis());
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al enviar el mensaje"));
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> getEstadisticasClinica() {
        try {
            Map<String, Object> estadisticas = new HashMap<>();
            
            // Años de experiencia
            estadisticas.put("añosExperiencia", 15);
            
            // Número de veterinarios
            long numVeterinarios = usuarioService.findAll().stream()
                    .filter(u -> u.getRol() != null &&
                               u.getRol().getNombre().equalsIgnoreCase("VETERINARIO"))
                    .count();
            estadisticas.put("numeroVeterinarios", numVeterinarios);
            
            // Especialidades
            List<String> especialidades = List.of(
                "Medicina General",
                "Cirugía",
                "Dermatología",
                "Oftalmología",
                "Cardiología",
                "Oncología",
                "Ortopedia"
            );
            estadisticas.put("especialidades", especialidades);
            
            // Certificaciones
            List<String> certificaciones = List.of(
                "Colegio Médico Veterinario del Perú",
                "Certificación en Bienestar Animal",
                "ISO 9001:2015"
            );
            estadisticas.put("certificaciones", certificaciones);
            
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas de la clínica"));
        }
    }
}
