package com.example.vehiculosdata.repositories;

import com.example.vehiculosdata.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
}
