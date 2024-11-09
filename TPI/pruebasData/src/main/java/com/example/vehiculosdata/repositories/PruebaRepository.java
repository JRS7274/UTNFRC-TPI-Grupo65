package com.example.vehiculosdata.repositories;

import com.example.vehiculosdata.models.Prueba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PruebaRepository extends JpaRepository<Prueba, Long> {
    boolean existsByVehiculoIdAndFechaHoraFinIsNull(Long vehiculoId);
    List<Prueba> findByFechaHoraFinIsNull();
    @Query("SELECT p FROM Prueba p WHERE p.vehiculoId = :vehicleId AND (p.fechaHoraFin IS NULL OR p.fechaHoraFin > CURRENT_TIMESTAMP)")
    public Prueba findActivePruebaByVehicleId(@Param("vehicleId") Long vehicleId);

}
