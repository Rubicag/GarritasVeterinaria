package com.mycompany.service;

import com.mycompany.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClinicaService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ServicioService servicioService;

    /**
     * Obtiene información general de la clínica
     */
    public Map<String, Object> getInformacionGeneralClinica() {
        Map<String, Object> info = new HashMap<>();
        
        // Información básica
        info.put("nombre", "Garritas Veterinaria");
        info.put("lema", "Cuidando a tus mejores amigos");
        info.put("director", "Dr. Juan Carlos Veterinario");
        info.put("licencia", "VET-2024-001234");
        info.put("añoFundacion", 2010);
        
        // Contacto
        Map<String, String> contacto = new HashMap<>();
        contacto.put("direccion", "Av. Principal 123, Lima, Perú");
        contacto.put("telefono", "+51 1 234-5678");
        contacto.put("celular", "+51 999 123-456");
        contacto.put("email", "info@garritasveterinaria.com");
        contacto.put("emergencias", "+51 1 999-8888");
        contacto.put("whatsapp", "+51 999 123-456");
        info.put("contacto", contacto);
        
        // Redes sociales
        Map<String, String> redesSociales = new HashMap<>();
        redesSociales.put("facebook", "@GarritasVeterinaria");
        redesSociales.put("instagram", "@garritas_veterinaria");
        redesSociales.put("twitter", "@garritasvet");
        redesSociales.put("website", "www.garritasveterinaria.com");
        info.put("redesSociales", redesSociales);
        
        return info;
    }

    /**
     * Obtiene los horarios de atención
     */
    public Map<String, Object> getHorariosAtencion() {
        Map<String, Object> horarios = new HashMap<>();
        
        // Horarios generales
        Map<String, String> horariosGenerales = new LinkedHashMap<>();
        horariosGenerales.put("lunes", "8:00 AM - 6:00 PM");
        horariosGenerales.put("martes", "8:00 AM - 6:00 PM");
        horariosGenerales.put("miercoles", "8:00 AM - 6:00 PM");
        horariosGenerales.put("jueves", "8:00 AM - 6:00 PM");
        horariosGenerales.put("viernes", "8:00 AM - 6:00 PM");
        horariosGenerales.put("sabado", "9:00 AM - 2:00 PM");
        horariosGenerales.put("domingo", "Cerrado (Solo emergencias)");
        
        horarios.put("horariosGenerales", horariosGenerales);
        
        // Servicios especiales
        Map<String, String> serviciosEspeciales = new HashMap<>();
        serviciosEspeciales.put("emergencias", "24 horas - 7 días a la semana");
        serviciosEspeciales.put("cirugia", "Lunes a Viernes: 9:00 AM - 4:00 PM");
        serviciosEspeciales.put("laboratorio", "Lunes a Viernes: 8:00 AM - 5:00 PM");
        serviciosEspeciales.put("estetica", "Martes y Jueves: 10:00 AM - 5:00 PM");
        serviciosEspeciales.put("radiologia", "Lunes a Viernes: 9:00 AM - 4:00 PM");
        
        horarios.put("serviciosEspeciales", serviciosEspeciales);
        
        // Información adicional
        horarios.put("telefonoEmergencias", "+51 1 999-8888");
        horarios.put("notaImportante", "Para emergencias fuera de horario, llamar al teléfono de emergencias");
        horarios.put("ultimaActualizacion", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return horarios;
    }

    /**
     * Obtiene estadísticas generales de la clínica
     */
    public Map<String, Object> getEstadisticasGenerales() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Contadores básicos
        estadisticas.put("totalUsuarios", usuarioService.countUsers());
        estadisticas.put("totalMascotas", mascotaService.count());
        estadisticas.put("totalProductos", productoService.findAll().size());
        estadisticas.put("totalServicios", servicioService.findAll().size());
        
        // Estadísticas de citas
        int totalCitas = citaService.listAll().size();
        int citasFuturas = citaService.findCitasFuturas().size();
        estadisticas.put("totalCitas", totalCitas);
        estadisticas.put("citasFuturas", citasFuturas);
        estadisticas.put("citasCompletadas", totalCitas - citasFuturas);
        
        // Personal
        long veterinarios = usuarioService.findAll().stream()
                .filter(u -> u.getRol() != null && u.getRol().getNombre().equalsIgnoreCase("VETERINARIO"))
                .count();
        long administradores = usuarioService.findAll().stream()
                .filter(u -> u.getRol() != null && u.getRol().getNombre().equalsIgnoreCase("ADMIN"))
                .count();
        
        estadisticas.put("totalVeterinarios", veterinarios);
        estadisticas.put("totalAdministradores", administradores);
        estadisticas.put("totalPersonal", veterinarios + administradores);
        
        // Valor del inventario
        double valorInventario = productoService.findAll().stream()
                .filter(p -> p.getPrecio() != null && p.getStock() != null)
                .mapToDouble(p -> p.getPrecio() * p.getStock())
                .sum();
        estadisticas.put("valorInventario", Math.round(valorInventario * 100.0) / 100.0);
        
        estadisticas.put("fechaGeneracion", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return estadisticas;
    }

    /**
     * Obtiene el equipo veterinario
     */
    public Map<String, Object> getEquipoVeterinario() {
        List<Usuario> personal = usuarioService.findAll().stream()
                .filter(u -> u.getRol() != null &&
                           (u.getRol().getNombre().equalsIgnoreCase("VETERINARIO") ||
                            u.getRol().getNombre().equalsIgnoreCase("ADMIN")))
                .collect(Collectors.toList());
        
        Map<String, Object> equipo = new HashMap<>();
        
        List<Map<String, Object>> veterinarios = personal.stream()
                .filter(u -> u.getRol().getNombre().equalsIgnoreCase("VETERINARIO"))
                .map(this::mapearVeterinario)
                .collect(Collectors.toList());
        
        List<Map<String, Object>> administradores = personal.stream()
                .filter(u -> u.getRol().getNombre().equalsIgnoreCase("ADMIN"))
                .map(this::mapearVeterinario)
                .collect(Collectors.toList());
        
        equipo.put("veterinarios", veterinarios);
        equipo.put("administradores", administradores);
        equipo.put("totalPersonal", personal.size());
        
        return equipo;
    }

    /**
     * Mapea un usuario a información de veterinario
     */
    private Map<String, Object> mapearVeterinario(Usuario usuario) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", usuario.getId());
        info.put("nombre", usuario.getUsuario());
        info.put("email", usuario.getCorreo());
        info.put("rol", usuario.getRol().getNombre());
        info.put("activo", true); // La entidad Usuario no tiene campo active
        info.put("fechaIngreso", null); // La entidad Usuario no tiene fecha de creación
        
        // Información adicional simulada (en un sistema real vendría de otra tabla)
        if (usuario.getRol().getNombre().equalsIgnoreCase("VETERINARIO")) {
            info.put("especialidad", getEspecialidadSimulada());
            info.put("experiencia", getExperienciaSimulada() + " años");
            info.put("consultorios", Arrays.asList("Consultorio 1", "Consultorio 2"));
        }
        
        return info;
    }

    private String getEspecialidadSimulada() {
        List<String> especialidades = Arrays.asList(
            "Medicina General", "Cirugía", "Dermatología", 
            "Oftalmología", "Cardiología", "Oncología"
        );
        return especialidades.get(new Random().nextInt(especialidades.size()));
    }

    private int getExperienciaSimulada() {
        return 2 + new Random().nextInt(15); // Entre 2 y 16 años
    }

    /**
     * Obtiene servicios disponibles organizados por categoría
     */
    public Map<String, Object> getServiciosDisponibles() {
        Map<String, Object> servicios = new HashMap<>();
        
        // Servicios médicos
        List<String> serviciosMedicos = Arrays.asList(
            "Consulta general veterinaria",
            "Vacunación completa",
            "Desparasitación interna y externa",
            "Control de peso y nutrición",
            "Medicina preventiva",
            "Geriatría veterinaria"
        );
        
        // Servicios quirúrgicos
        List<String> serviciosQuirurgicos = Arrays.asList(
            "Esterilización (castración)",
            "Cirugía de tejidos blandos",
            "Cirugía ortopédica",
            "Cirugía oncológica",
            "Odontología veterinaria",
            "Cirugías de emergencia"
        );
        
        // Servicios de diagnóstico
        List<String> serviciosDiagnostico = Arrays.asList(
            "Radiografías digitales",
            "Ecografías abdominales",
            "Análisis de sangre completos",
            "Análisis de orina",
            "Biopsias y citologías",
            "Electrocardiogramas"
        );
        
        // Servicios de estética
        List<String> serviciosEstetica = Arrays.asList(
            "Baño medicinal y cosmético",
            "Corte de pelo profesional",
            "Corte y limado de uñas",
            "Limpieza de oídos",
            "Tratamiento antipulgas",
            "Aromaterapia para mascotas"
        );
        
        // Servicios de emergencia
        List<String> serviciosEmergencia = Arrays.asList(
            "Atención 24 horas",
            "Hospitalización",
            "Cuidados intensivos",
            "Estabilización de pacientes críticos",
            "Cirugía de emergencia",
            "Soporte respiratorio"
        );
        
        servicios.put("medicos", serviciosMedicos);
        servicios.put("quirurgicos", serviciosQuirurgicos);
        servicios.put("diagnostico", serviciosDiagnostico);
        servicios.put("estetica", serviciosEstetica);
        servicios.put("emergencia", serviciosEmergencia);
        
        // Información adicional
        servicios.put("totalCategorias", 5);
        servicios.put("totalServicios", serviciosMedicos.size() + serviciosQuirurgicos.size() + 
                                       serviciosDiagnostico.size() + serviciosEstetica.size() + 
                                       serviciosEmergencia.size());
        
        return servicios;
    }

    /**
     * Obtiene certificaciones y acreditaciones de la clínica
     */
    public Map<String, Object> getCertificacionesYAcreditaciones() {
        Map<String, Object> certificaciones = new HashMap<>();
        
        List<Map<String, Object>> certificacionesList = Arrays.asList(
            Map.of(
                "nombre", "Colegio Médico Veterinario del Perú",
                "codigo", "CMVP-2024-001",
                "vigencia", "2025-12-31",
                "tipo", "Habilitación Profesional"
            ),
            Map.of(
                "nombre", "Certificación en Bienestar Animal",
                "codigo", "CBA-2024-045",
                "vigencia", "2026-06-30",
                "tipo", "Especialización"
            ),
            Map.of(
                "nombre", "ISO 9001:2015 - Gestión de Calidad",
                "codigo", "ISO-9001-2024",
                "vigencia", "2027-01-15",
                "tipo", "Certificación Internacional"
            ),
            Map.of(
                "nombre", "SENASA - Registro Sanitario",
                "codigo", "SENASA-VET-2024-123",
                "vigencia", "2025-12-31",
                "tipo", "Registro Sanitario"
            )
        );
        
        certificaciones.put("certificaciones", certificacionesList);
        certificaciones.put("totalCertificaciones", certificacionesList.size());
        certificaciones.put("ultimaActualizacion", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return certificaciones;
    }

    /**
     * Obtiene información de contacto y ubicación
     */
    public Map<String, Object> getContactoYUbicacion() {
        Map<String, Object> contacto = new HashMap<>();
        
        // Dirección principal
        Map<String, String> direccion = new HashMap<>();
        direccion.put("calle", "Av. Principal 123");
        direccion.put("distrito", "Lima Centro");
        direccion.put("ciudad", "Lima");
        direccion.put("region", "Lima");
        direccion.put("pais", "Perú");
        direccion.put("codigoPostal", "15001");
        direccion.put("referencia", "Frente al parque central, al lado del banco");
        
        // Teléfonos
        Map<String, String> telefonos = new HashMap<>();
        telefonos.put("principal", "+51 1 234-5678");
        telefonos.put("emergencias", "+51 1 999-8888");
        telefonos.put("whatsapp", "+51 999 123-456");
        telefonos.put("fax", "+51 1 234-5679");
        
        // Emails
        Map<String, String> emails = new HashMap<>();
        emails.put("general", "info@garritasveterinaria.com");
        emails.put("citas", "citas@garritasveterinaria.com");
        emails.put("emergencias", "emergencias@garritasveterinaria.com");
        emails.put("administracion", "admin@garritasveterinaria.com");
        
        contacto.put("direccion", direccion);
        contacto.put("telefonos", telefonos);
        contacto.put("emails", emails);
        
        // Coordenadas GPS (simuladas)
        contacto.put("coordenadas", Map.of(
            "latitud", -12.0464,
            "longitud", -77.0428
        ));
        
        return contacto;
    }
}
