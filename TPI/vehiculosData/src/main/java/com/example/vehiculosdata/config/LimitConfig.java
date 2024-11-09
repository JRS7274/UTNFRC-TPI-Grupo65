package com.example.vehiculosdata.config;

import com.example.vehiculosdata.models.Coordenadas;
import com.example.vehiculosdata.models.Zona;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LimitConfig {
    private Coordenadas coordenadasAgencia;
    private double radioAdmitidoKm;
    private List<Zona> zonasRestringidas;
}
