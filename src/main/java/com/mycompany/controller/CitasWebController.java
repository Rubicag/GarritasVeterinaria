package com.mycompany.controller;

import com.mycompany.model.Cita;
import com.mycompany.model.Mascota;
import com.mycompany.model.Servicio;
import com.mycompany.service.CitaService;
import com.mycompany.repository.MascotaRepository;
import com.mycompany.repository.ServicioRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CitasWebController {

    private final CitaService citaService;
    private final MascotaRepository mascotaRepo;
    private final ServicioRepository servicioRepo;

    public CitasWebController(CitaService citaService, MascotaRepository mascotaRepo, ServicioRepository servicioRepo) {
        this.citaService = citaService;
        this.mascotaRepo = mascotaRepo;
        this.servicioRepo = servicioRepo;
    }

    @GetMapping("/citas")
    public String viewCitas(Model model) {
        List<Cita> citas = citaService.listAll();
        List<Mascota> mascotas = mascotaRepo.findAll();
        List<Servicio> servicios = servicioRepo.findAll();
        model.addAttribute("citas", citas);
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("servicios", servicios);
        model.addAttribute("nuevaCita", new Cita());
        return "citas";
    }

    @PostMapping("/citas")
    public String crearCita(@ModelAttribute("nuevaCita") Cita c, 
                           @ModelAttribute("mascotaId") Long mascotaId,
                           @ModelAttribute("servicioId") Long servicioId) {
        try {
            citaService.create(c, mascotaId, servicioId);
            return "redirect:/citas?success=true";
        } catch (Exception e) {
            return "redirect:/citas?error=" + e.getMessage();
        }
    }
}
