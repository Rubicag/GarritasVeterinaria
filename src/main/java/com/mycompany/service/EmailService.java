package com.mycompany.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio para envío de notificaciones por email.
 * Configuración en application.properties:
 * - spring.mail.host
 * - spring.mail.port
 * - spring.mail.username
 * - spring.mail.password
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@garritas-veterinaria.com}")
    private String fromEmail;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía un email simple.
     * @param to Destinatario
     * @param subject Asunto
     * @param text Contenido del mensaje
     */
    public void enviarEmail(String to, String subject, String text) {
        if (!emailEnabled || mailSender == null) {
            logger.info("Email deshabilitado o no configurado. No se enviará email a: {}", to);
            logger.info("Asunto: {}", subject);
            logger.info("Contenido: {}", text);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            logger.info("Email enviado exitosamente a: {}", to);
        } catch (Exception e) {
            logger.error("Error al enviar email a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error al enviar email", e);
        }
    }

    /**
     * Envía recordatorio de cita.
     */
    public void enviarRecordatorioCita(String email, String nombrePaciente, 
                                      String fechaCita, String servicio) {
        String subject = "Recordatorio de Cita - Veterinaria Garritas";
        String text = String.format(
            "Estimado/a propietario/a de %s,\n\n" +
            "Le recordamos que tiene una cita programada:\n\n" +
            "Fecha y hora: %s\n" +
            "Servicio: %s\n\n" +
            "Por favor, llegue 10 minutos antes de su cita.\n\n" +
            "Si necesita cancelar o reprogramar, contáctenos lo antes posible.\n\n" +
            "Atentamente,\n" +
            "Veterinaria Garritas",
            nombrePaciente, fechaCita, servicio
        );
        
        enviarEmail(email, subject, text);
    }

    /**
     * Envía confirmación de cita creada.
     */
    public void enviarConfirmacionCita(String email, String nombrePaciente, 
                                       String fechaCita, String servicio) {
        String subject = "Confirmación de Cita - Veterinaria Garritas";
        String text = String.format(
            "Estimado/a propietario/a de %s,\n\n" +
            "Su cita ha sido registrada exitosamente:\n\n" +
            "Fecha y hora: %s\n" +
            "Servicio: %s\n\n" +
            "Recibirá un recordatorio 24 horas antes de su cita.\n\n" +
            "Atentamente,\n" +
            "Veterinaria Garritas",
            nombrePaciente, fechaCita, servicio
        );
        
        enviarEmail(email, subject, text);
    }
}
