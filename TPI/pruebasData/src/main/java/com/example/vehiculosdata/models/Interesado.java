package com.example.vehiculosdata.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "interesados")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Interesado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo_documento;
    private String documento;
    private String nombre;
    private String apellido;
    private Boolean restringido;
    private Integer nro_licencia;
    private LocalDate fecha_vencimiento_licencia;

}
