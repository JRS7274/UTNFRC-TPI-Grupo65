package com.example.vehiculosdata.controllers;

import com.example.vehiculosdata.models.Modelo;
import com.example.vehiculosdata.models.Vehiculo;
import com.example.vehiculosdata.repositories.VehiculoRepository;
import com.example.vehiculosdata.services.ModeloService;
import com.example.vehiculosdata.services.VehiculoService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {
    private VehiculoService vehiculoService;

    @Autowired
    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping
    public ResponseEntity<Iterable<Vehiculo>> getVehiculos() {
        return ResponseEntity.ok(vehiculoService.getAllVehiculos());
    }

    @GetMapping("{id}")
    public ResponseEntity<Vehiculo> getVehiculoById(@PathVariable("id") Long id) {
        try{
            Vehiculo vehiculo = vehiculoService.getVehiculoById(id);
            return ResponseEntity.ok(vehiculo);
        } catch (Exception e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @PostMapping
    public ResponseEntity<Vehiculo> addVehiculo(@RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.save(vehiculo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> updateVehiculo(@PathVariable("id") Long id, @RequestBody Vehiculo vehiculoDetails) {
        try{
            Vehiculo vehiculo = vehiculoService.getVehiculoById(id);
            vehiculo.setPatente(vehiculoDetails.getPatente());
            vehiculo.setModelo(vehiculoDetails.getModelo());
            vehiculo.setAnio(vehiculoDetails.getAnio());
            return ResponseEntity.ok(vehiculoService.save(vehiculo));
        } catch (ServiceException e){
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteVehiculoById(@PathVariable("id") Long id) {
        try{
            Vehiculo vehiculo = vehiculoService.getVehiculoById(id);
            vehiculoService.delete(vehiculo.getId());
            return ResponseEntity.ok("Vehiculo con ID " + id + " borrado exitosamente.");
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }
}
