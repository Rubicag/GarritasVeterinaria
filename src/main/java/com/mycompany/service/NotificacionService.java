package com.mycompany.service;

import com.mycompany.model.Cita;
import com.mycompany.repository.CitaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para tareas programadas de notificaciones.
 */
@Service
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final CitaRepository citaRepository;
    private final EmailService emailService;

    public NotificacionService(CitaRepository citaRepository, EmailService emailService) {
        this.citaRepository = citaRepository;
        this.emailService = emailService;
    }

    /**
     * Envía recordatorios de citas programadas para las próximas 24 horas.
     * Se ejecuta diariamente a las 9:00 AM.
     */
    @Scheduled(cron = "0 0 9 * * *") // Cada día a las 9:00 AM
    public void enviarRecordatoriosCitasDiarias() {
        logger.info("Iniciando envío de recordatorios de citas...");
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime dentro24h = ahora.plusHours(24);
        
        // Buscar citas en las próximas 24 horas
        List<Cita> citas = citaRepository.findByFechaBetween(ahora, dentro24h);
        
        int emailsEnviados = 0;
        for (Cita cita : citas) {
            try {
                if (cita.getMascota() != null && 
                    cita.getMascota().getPropietario() != null &&
                    cita.getMascota().getPropietario().getCorreo() != null) {
                    
                    String email = cita.getMascota().getPropietario().getCorreo();
                    String nombreMascota = cita.getMascota().getNombre();
                    String fechaFormateada = cita.getFecha().format(FORMATTER);
                    String servicio = cita.getServicio() != null ? 
                        cita.getServicio().getNombre() : "Consulta general";
                    
                    emailService.enviarRecordatorioCita(email, nombreMascota, 
                        fechaFormateada, servicio);
                    
                    emailsEnviados++;
                }
            } catch (Exception e) {
                logger.error("Error al enviar recordatorio para cita {}: {}", 
                    cita.getId(), e.getMessage());
            }
        }
        
        logger.info("Recordatorios enviados: {} de {} citas", emailsEnviados, citas.size());
    }

    /**
     * Envía confirmación de cita inmediatamente después de crearla.
     * Este método debe ser llamado desde CitaService después de guardar una cita.
     */
    public void enviarConfirmacionCita(Cita cita) {
        try {
            if (cita.getMascota() != null && 
                cita.getMascota().getPropietario() != null &&
                cita.getMascota().getPropietario().getCorreo() != null) {
                
                String email = cita.getMascota().getPropietario().getCorreo();
                String nombreMascota = cita.getMascota().getNombre();
                String fechaFormateada = cita.getFecha().format(FORMATTER);
                String servicio = cita.getServicio() != null ? 
                    cita.getServicio().getNombre() : "Consulta general";
                
                emailService.enviarConfirmacionCita(email, nombreMascota, 
                    fechaFormateada, servicio);
            }
        } catch (Exception e) {
            logger.error("Error al enviar confirmación para cita {}: {}", 
                cita.getId(), e.getMessage());
        }
    }
}
