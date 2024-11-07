package com.example.vehiculosdata.repositories;

import com.example.vehiculosdata.models.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
