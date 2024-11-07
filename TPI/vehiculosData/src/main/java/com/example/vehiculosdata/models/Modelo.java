package com.example.vehiculosdata.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "modelos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_MARCA", nullable = false)
    private Marca marca;

    @OneToMany(mappedBy = "modelo")
    private Set<Vehiculo> vehiculos;

    private String descripcion;
}
