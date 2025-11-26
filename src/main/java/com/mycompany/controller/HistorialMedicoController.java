package com.mycompany.controller;

import com.mycompany.model.HistorialMedico;
import com.mycompany.service.HistorialMedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controlador REST para el historial médico.
 * Usa la tabla historial_medico.
 */
@RestController
@RequestMapping("/api/historial-medico")
@Tag(name = "Historial Médico", description = "API para gestión del historial clínico de mascotas")
public class HistorialMedicoController {

    private final HistorialMedicoService historialService;

    public HistorialMedicoController(HistorialMedicoService historialService) {
        this.historialService = historialService;
    }

    @Operation(summary = "Listar todos los historiales médicos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<HistorialMedico>> listarTodos() {
        return ResponseEntity.ok(historialService.listarTodos());
    }

    @Operation(summary = "Obtener historial médico por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial encontrado"),
        @ApiResponse(responseCode = "404", description = "Historial no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HistorialMedico> obtenerPorId(@PathVariable Long id) {
        return historialService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener historial médico completo de una mascota")
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<HistorialMedico>> obtenerPorMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(historialService.obtenerPorMascota(mascotaId));
    }

    @Operation(summary = "Crear un nuevo registro en el historial médico")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Registro creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error al procesar el archivo")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crear(
            @RequestParam("citaId") Long citaId,
            @RequestParam("diagnostico") String diagnostico,
            @RequestParam("tratamiento") String tratamiento,
            @RequestParam(value = "observaciones", required = false) String observaciones,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo) {
        
        try {
            HistorialMedico historial = historialService.crear(
                citaId, diagnostico, tratamiento, observaciones, archivo
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(historial);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar un registro del historial médico")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestParam(required = false) String diagnostico,
            @RequestParam(required = false) String tratamiento,
            @RequestParam(required = false) String observaciones,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo) {
        
        try {
            HistorialMedico historial = historialService.actualizar(
                id, diagnostico, tratamiento, observaciones, archivo
            );
            return ResponseEntity.ok(historial);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Descargar archivo adjunto del historial médico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Archivo descargado"),
        @ApiResponse(responseCode = "404", description = "Archivo no encontrado")
    })
    @GetMapping("/{id}/archivo")
    public ResponseEntity<byte[]> descargarArchivo(@PathVariable Long id) {
        return historialService.obtenerPorId(id)
                .filter(h -> h.getArchivo() != null)
                .map(historial -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(historial.getTipoArchivo()));
                    headers.setContentDisposition(
                        ContentDisposition.attachment()
                            .filename(historial.getNombreArchivo())
                            .build()
                    );
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(historial.getArchivo());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un registro del historial médico")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro eliminado"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (historialService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
