package com.example.vehiculosdata.controllers;

import com.example.vehiculosdata.models.Prueba;
import com.example.vehiculosdata.services.PruebaService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/pruebas")
public class PruebaController {
    private final PruebaService pruebaService;

    @Autowired
    public PruebaController(PruebaService pruebaService) {
        this.pruebaService = pruebaService;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    /*
    public ResponseEntity<Iterable<Prueba>> getAllPruebas() {
        return ResponseEntity.ok(pruebaService.getAllPruebas());
    }
     */

    @GetMapping
    public ResponseEntity<?> getAllPruebas() {
        Iterable<Prueba> pruebas = pruebaService.getAllPruebas();

        if (!pruebas.iterator().hasNext()) {
            return ResponseEntity.ok("No Pruebas found.");
        }

        return ResponseEntity.ok(pruebas);
    }

    @GetMapping("{id}")
    public ResponseEntity<Prueba> getPruebaById(@PathVariable("id") Long id) {
        try{
            Prueba prueba = pruebaService.getPruebaById(id);
            return ResponseEntity.ok(prueba);
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }
}
