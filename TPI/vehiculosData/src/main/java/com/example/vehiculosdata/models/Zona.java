package com.example.vehiculosdata.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zona {
    private Coordenadas noroeste;
    private Coordenadas sureste;
}
