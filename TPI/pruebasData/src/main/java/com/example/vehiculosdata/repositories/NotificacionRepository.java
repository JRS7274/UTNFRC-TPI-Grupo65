package com.example.vehiculosdata.repositories;

import com.example.vehiculosdata.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}
