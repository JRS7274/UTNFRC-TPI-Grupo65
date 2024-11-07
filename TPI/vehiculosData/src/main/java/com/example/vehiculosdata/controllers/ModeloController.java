package com.example.vehiculosdata.controllers;

import com.example.vehiculosdata.models.Modelo;
import com.example.vehiculosdata.repositories.ModeloRepository;
import com.example.vehiculosdata.services.ModeloService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/modelos")
public class ModeloController {
    private ModeloService modeloService;

    @Autowired
    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping
    public ResponseEntity<Iterable<Modelo>> getModelos() {
        return ResponseEntity.ok(modeloService.getAllModelos());
    }

    @GetMapping("{id}")
    public ResponseEntity<Modelo> getModeloById(@PathVariable("id") Long id) {
        try{
            Modelo modelo = modeloService.getModeloById(id);
            return ResponseEntity.ok(modelo);
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @PostMapping
    public ResponseEntity<Modelo> createModelo(@RequestBody Modelo modelo) {
        return ResponseEntity.ok(modeloService.createModelo(modelo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Modelo> updateModelo(@PathVariable("id") Long id, @RequestBody Modelo modeloDetails) {
        try{
            Modelo found = modeloService.getModeloById(id);
            found.setMarca(modeloDetails.getMarca());
            found.setVehiculos(modeloDetails.getVehiculos());
            found.setDescripcion(modeloDetails.getDescripcion());
            return ResponseEntity.ok(modeloService.createModelo(found));
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteModelo(@PathVariable("id") Long id) {
        try{
            Modelo modelo = modeloService.getModeloById(id);
            modeloService.deleteModelo(id);
            return ResponseEntity.ok("Modelo con ID " + id + " borrado exitosamente.");
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }
}
