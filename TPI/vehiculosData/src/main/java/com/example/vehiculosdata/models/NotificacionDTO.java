package com.example.vehiculosdata.models;

import lombok.Data;

@Data
public class NotificacionDTO {
    private Long posicionId;
    private String mensaje;
    private Long empleadoId;
    private Long interesadoId;
    private String numero;  // assuming it's a String for phone numbers
}
