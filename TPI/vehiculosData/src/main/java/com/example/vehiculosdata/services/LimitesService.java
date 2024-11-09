package com.example.vehiculosdata.services;

import com.example.vehiculosdata.config.LimitConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LimitesService {
    private final RestTemplate restTemplate;

    @Autowired
    public LimitesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LimitConfig getLimitConfig() {
        String url = "https://labsys.frc.utn.edu.ar/apps-disponibilizadas/backend/api/v1/configuracion/";

        ResponseEntity<LimitConfig> response = restTemplate
                .exchange(url, HttpMethod.GET, null, LimitConfig.class);

        return response.getBody();
    }
}
