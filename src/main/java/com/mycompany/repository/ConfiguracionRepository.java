package com.mycompany.repository;

import com.mycompany.model.Configuracion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
    Optional<Configuracion> findByClave(String clave);
}
