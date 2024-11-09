package com.example.vehiculosdata.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "posicion_id")
    private Long posicionId;

    private String mensaje;

    private Long numero;

    @Column(name = "empleado_id")
    private Long empleadoID;

    @Column(name = "interesado_id")
    private Long interesadoID;
}
