package com.example.vehiculosdata.controllers;


import com.example.vehiculosdata.models.Posicion;
import com.example.vehiculosdata.models.Vehiculo;
import com.example.vehiculosdata.services.VehiculoPosicionService;
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
    private VehiculoPosicionService vehiculoPosicionService;

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

    // Endpoint to retrieve the current position of a vehicle
    @GetMapping("/{vehiculoId}/current-position")
    public ResponseEntity<Posicion> getCurrentPosition(@PathVariable Long vehiculoId) {
        Posicion currentPosition = vehiculoPosicionService.getCurrentPosition(vehiculoId);
        if (currentPosition == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(currentPosition);
    }

    @GetMapping("/checkLimits/{vehicleId}")
    public ResponseEntity<Boolean> checkVehicleLimits(@PathVariable Long vehicleId) {
        boolean isInLimits = vehiculoService.isVehicleInLimits(vehicleId);
        System.out.println("Endpoint - Vehicle ID " + vehicleId + " is in limits: " + isInLimits);
        return ResponseEntity.ok(isInLimits);
        //Cuando este metodo devuelve true significa que el vehiculo esta dentro de los limites
    }
}
