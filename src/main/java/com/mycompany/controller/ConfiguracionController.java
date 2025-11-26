package com.mycompany.controller;

import com.mycompany.service.ConfiguracionService;
import java.security.Principal;
import java.util.Map;
import com.mycompany.controller.dto.ConfigForm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping("/configuracion")
    public String configuracion(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        boolean admin = isAdmin();
        model.addAttribute("isAdmin", admin);

        // load sample keys used in UI
        String siteName = configuracionService.get("site.name", "Garritas Veterinaria");
        String maintenance = configuracionService.get("site.maintenance", "false");
        String itemsPerPage = configuracionService.get("site.itemsPerPage", "20");

        model.addAttribute("siteName", siteName);
        model.addAttribute("maintenance", maintenance);
        model.addAttribute("itemsPerPage", itemsPerPage);

        Map<String, String> all = configuracionService.getAll();
        model.addAttribute("allConfigs", all);

        // prepare form-backing object for Thymeleaf form binding
        ConfigForm form = new ConfigForm();
        form.setSiteName(siteName);
        form.setMaintenance("true".equalsIgnoreCase(maintenance));
        try {
            form.setItemsPerPage(Integer.valueOf(itemsPerPage));
        } catch (NumberFormatException e) {
            form.setItemsPerPage(20);
        }
        model.addAttribute("configForm", form);

        return "configuracion";
    }

    @PostMapping("/configuracion")
    public String guardarConfiguracion(@Valid @ModelAttribute("configForm") com.mycompany.controller.dto.ConfigForm form,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        if (!isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado: solo administradores pueden modificar la configuración.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isAdmin", true);
            model.addAttribute("siteName", form.getSiteName());
            model.addAttribute("maintenance", String.valueOf(form.isMaintenance()));
            model.addAttribute("itemsPerPage", form.getItemsPerPage() != null ? form.getItemsPerPage().toString() : "20");
            model.addAttribute("allConfigs", configuracionService.getAll());
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "configuracion";
        }

        configuracionService.save("site.name", form.getSiteName() != null ? form.getSiteName() : "");
        configuracionService.save("site.maintenance", form.isMaintenance() ? "true" : "false");
        configuracionService.save("site.itemsPerPage", form.getItemsPerPage() != null ? String.valueOf(form.getItemsPerPage()) : "20");

        redirectAttributes.addFlashAttribute("successMessage", "Configuración guardada correctamente.");
        return "redirect:/configuracion";
    }
}
