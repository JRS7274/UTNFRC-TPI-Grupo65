package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Posicion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificacionService {

    private final RestTemplate restTemplate;

    @Value("${notificacion.service.url}")
    private String notificacionServiceUrl;

    public NotificacionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createOffLimitNotification(Long vehicleId, Posicion posicion, String message) {
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("vehiculoId", vehicleId);
        notificationData.put("posicionId", posicion.getId());
        notificationData.put("mensaje", message);
        notificationData.put("latitud", posicion.getLat());
        notificationData.put("longitud", posicion.getLon());

        // Assuming the notification service URL is configured in application.properties
        restTemplate.postForObject(notificacionServiceUrl + "/notificaciones", notificationData, Void.class);
    }
}
