package com.example.vehiculosdata.controllers;

import com.example.vehiculosdata.models.Posicion;
import com.example.vehiculosdata.services.PosicionService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/vehiculos/posiciones")
public class PosicionController {
    private PosicionService posicionService;

    @Autowired
    public PosicionController(PosicionService posicionService) {
        this.posicionService = posicionService;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping
    public ResponseEntity<Iterable<Posicion>> getPosiciones() {
        Iterable<Posicion> posiciones = posicionService.getPosiciones();
        return ResponseEntity.ok(posiciones);
    }

    @PostMapping
    public ResponseEntity<Posicion> addPosicion(Posicion posicion) {
        return ResponseEntity.ok(posicionService.createPosicion(posicion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePosicion(@PathVariable Long id) {
        try{
            Posicion posicion = posicionService.getPosicionById(id);
            posicionService.deletePosicion(posicion);
            return ResponseEntity.ok("Posicion con ID " + id + " borrado exitosamente.");
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }
}
