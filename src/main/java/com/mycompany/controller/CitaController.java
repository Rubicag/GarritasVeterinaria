package com.mycompany.controller;

import com.mycompany.dto.CitaRequestDTO;
import com.mycompany.dto.CitaResponseDTO;
import com.mycompany.model.Cita;
import com.mycompany.model.Usuario;
import com.mycompany.service.CitaService;
import com.mycompany.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
@Tag(name = "Citas", description = "API para gestión de citas veterinarias")
public class CitaController {

    private final CitaService citaService;
    private final UsuarioRepository usuarioRepository;

    public CitaController(CitaService citaService, UsuarioRepository usuarioRepository) { 
        this.citaService = citaService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(
        summary = "Listar todas las citas",
        description = "Obtiene el listado completo de citas registradas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de citas obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> list() { 
        try {
            List<Cita> citas = citaService.listAll();
            List<CitaResponseDTO> citasDTO = citas.stream()
                .map(CitaResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(citasDTO);
        } catch (Exception e) {
            System.err.println("Error al listar citas: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
        summary = "Obtener cita por ID",
        description = "Obtiene los detalles de una cita específica por su identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita encontrada"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> get(
        @Parameter(description = "ID de la cita", required = true)
        @PathVariable Long id) {
        try {
            Optional<Cita> c = citaService.getById(id);
            return c.map(cita -> ResponseEntity.ok(new CitaResponseDTO(cita)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
        summary = "Crear nueva cita",
        description = "Registra una nueva cita en el sistema. Si no se especifica veterinario, se asigna automáticamente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cita creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de cita inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la cita a crear",
            required = true,
            content = @Content(schema = @Schema(implementation = CitaRequestDTO.class))
        )
        @Valid @RequestBody CitaRequestDTO citaDTO) {
        try {
            Cita cita = new Cita();
            cita.setFecha(citaDTO.getFecha());
            cita.setObservaciones(citaDTO.getObservaciones());
            
            // Asignar veterinario manualmente si se proporciona
            if (citaDTO.getVeterinarioId() != null) {
                Optional<Usuario> veterinario = usuarioRepository.findById(citaDTO.getVeterinarioId());
                veterinario.ifPresent(cita::setVeterinario);
            }
            
            Cita created = citaService.create(cita, citaDTO.getMascotaId(), citaDTO.getServicioId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new CitaResponseDTO(created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Cita c) {
        try {
            Cita updated = citaService.update(id, c);
            if (updated == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(new CitaResponseDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (!citaService.delete(id)) return ResponseEntity.notFound().build();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @Operation(
        summary = "Listar citas futuras",
        description = "Obtiene todas las citas programadas para fechas futuras"
    )
    @GetMapping("/futuras")
    public ResponseEntity<List<CitaResponseDTO>> getCitasFuturas() {
        try {
            List<Cita> citas = citaService.findCitasFuturas();
            List<CitaResponseDTO> citasDTO = citas.stream()
                .map(CitaResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(citasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/por-dia")
    public ResponseEntity<List<CitaResponseDTO>> getCitasPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        try {
            List<Cita> citas = citaService.findCitasPorDia(fecha);
            List<CitaResponseDTO> citasDTO = citas.stream()
                .map(CitaResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(citasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarCita(@PathVariable Long id) {
        try {
            boolean confirmada = citaService.confirmarCita(id);
            if (confirmada) {
                return ResponseEntity.ok(Map.of("message", "Cita confirmada exitosamente"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }
}

