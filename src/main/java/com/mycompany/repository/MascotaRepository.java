package com.mycompany.repository;

import com.mycompany.model.Mascota;
import com.mycompany.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByPropietario(Usuario propietario);
    List<Mascota> findByEspecie(String especie);
    List<Mascota> findByNombreContainingIgnoreCase(String nombre);
}
