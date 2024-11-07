package com.example.vehiculosdata.repositories;

import com.example.vehiculosdata.models.Modelo;
import org.springframework.data.repository.CrudRepository;

public interface ModeloRepository extends CrudRepository<Modelo, Long> {
    //Modelo findByDescripcion(String nombre);
}
