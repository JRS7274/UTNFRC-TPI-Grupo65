package com.example.vehiculosdata.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pruebas")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Prueba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ID_VEHICULO")
    private Long vehiculoId;

    @Column(name = "ID_INTERESADO")
    private Long interesadoId;

    @Column(name = "ID_EMPLEADO")
    private Long empleadoId;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String comentarios;

}
