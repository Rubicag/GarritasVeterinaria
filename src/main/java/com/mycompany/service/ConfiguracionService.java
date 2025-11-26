package com.mycompany.service;

import com.mycompany.model.Configuracion;
import com.mycompany.repository.ConfiguracionRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfiguracionService {

    private final ConfiguracionRepository repo;

    public ConfiguracionService(ConfiguracionRepository repo) {
        this.repo = repo;
    }

    public String get(String clave, String defaultValue) {
        return repo.findByClave(clave).map(Configuracion::getValor).orElse(defaultValue);
    }

    public Map<String, String> getAll() {
        Map<String, String> map = new LinkedHashMap<>();
        List<Configuracion> all = repo.findAll();
        for (Configuracion c : all) {
            map.put(c.getClave(), c.getValor());
        }
        return map;
    }

    public void save(String clave, String valor) {
        Optional<Configuracion> existing = repo.findByClave(clave);
        Configuracion c = existing.orElseGet(() -> new Configuracion(clave, valor));
        c.setValor(valor);
        repo.save(c);
    }
}
