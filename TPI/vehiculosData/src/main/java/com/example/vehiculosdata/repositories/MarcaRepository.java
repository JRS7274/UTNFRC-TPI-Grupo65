package com.example.vehiculosdata.repositories;

import com.example.vehiculosdata.models.Marca;
import org.springframework.data.repository.CrudRepository;

public interface MarcaRepository extends CrudRepository<Marca, Long> {
    Marca findByNombre(String descripcion);
}
