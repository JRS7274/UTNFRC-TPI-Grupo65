package com.example.vehiculosdata.repositories;

import com.example.vehiculosdata.models.Posicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosicionRepository extends JpaRepository<Posicion, Long> {
    List<Posicion> findByVehiculoId(Long vehiculoId);
    @Query("SELECT p FROM Posicion p WHERE p.vehiculo.id = :vehiculoId ORDER BY p.fechaHora DESC")
    List<Posicion> findPositionsForVehicle(@Param("vehiculoId") Long vehiculoId);
}
