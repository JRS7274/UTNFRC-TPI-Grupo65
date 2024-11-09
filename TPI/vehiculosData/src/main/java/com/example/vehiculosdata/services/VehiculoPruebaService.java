package com.example.vehiculosdata.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VehiculoPruebaService {
    private final RestTemplate restTemplate;

    // Inject RestTemplate via constructor
    @Autowired
    public VehiculoPruebaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}