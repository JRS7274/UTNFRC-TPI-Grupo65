package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Prueba;
import com.example.vehiculosdata.repositories.PruebaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PruebaService {
    private PruebaRepository pruebaRepository;

    @Autowired
    public PruebaService(PruebaRepository pruebaRepository) { this.pruebaRepository = pruebaRepository; }

    public Iterable<Prueba> getAllPruebas() {
        return pruebaRepository.findAll();
    }

    public Prueba getPruebaById(Long id) {
        return pruebaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prueba no encontrada"));
    }
}
