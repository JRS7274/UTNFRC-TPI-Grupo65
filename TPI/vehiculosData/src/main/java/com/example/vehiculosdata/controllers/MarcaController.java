package com.example.vehiculosdata.controllers;

import com.example.vehiculosdata.models.Marca;
import com.example.vehiculosdata.services.MarcaService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/marcas")
public class MarcaController {
    private final MarcaService marcaService;

    @Autowired
    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Marca>> getAllMarcas() {
        return ResponseEntity.ok(marcaService.getMarcas());
    }

    @GetMapping("{id}")
    public ResponseEntity<Marca> getMarcaById(@PathVariable("id") Long id) {
        try{
            Marca found = marcaService.getById(id);
            return ResponseEntity.ok(found);
        } catch (ServiceException e){
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @GetMapping("nombre/{nombre}")
    public ResponseEntity<Marca> getMarcasByNombre(@PathVariable("nombre") String nombre) {
        return ResponseEntity.ok(marcaService.getMarcaByNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<Marca> createMarca(@RequestBody Marca marca) {
        return ResponseEntity.ok(marcaService.create(marca));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> updateMarca(@PathVariable("id") Long id, @RequestBody Marca marcaDetails) {
        try{
            Marca found = marcaService.getById(id);
            found.setNombre(marcaDetails.getNombre());
            return ResponseEntity.ok(marcaService.create(found));
        } catch (ServiceException e){
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMarca(@PathVariable("id") Long id) {
        try{
            Marca found = marcaService.getById(id);
            marcaService.delete(id);
            return ResponseEntity.ok("Marca con ID " + id + " borrada exitosamente.");
        } catch (ServiceException e){
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }
}
