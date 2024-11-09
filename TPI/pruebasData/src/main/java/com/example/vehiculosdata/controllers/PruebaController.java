package com.example.vehiculosdata.controllers;

import com.example.vehiculosdata.models.Prueba;
import com.example.vehiculosdata.repositories.PruebaRepository;
import com.example.vehiculosdata.services.PruebaService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/pruebas")
public class PruebaController {
    private final PruebaService pruebaService;

    @Autowired
    private PruebaRepository pruebaRepository;

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

    @GetMapping("/enCurso")
    public Object getPruebasActivas(){
        Iterable<Prueba> pruebasEnCurso = pruebaService.getPruebasEnCurso();
        if (!pruebasEnCurso.iterator().hasNext()) {
            return ResponseEntity.ok("No hay pruebas en curso");
        }
        return pruebaService.getPruebasEnCurso();
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

    @GetMapping("/verificar-cliente/{interesadoId}")
    public boolean verificarCliente(@PathVariable Long interesadoId) {
        return pruebaService.verificarCliente(interesadoId);
    }

    @PostMapping
    public ResponseEntity<Prueba> addPrueba(@RequestBody Prueba prueba) {
        return ResponseEntity.ok(pruebaService.save(prueba, true));
    }

    // Endpoint to check if a vehicle is in use
    @GetMapping("/vehiculo/{id}")
    public boolean isVehiculoInUse(@PathVariable Long id) {
        return pruebaService.isVehiculoInUse(id);
    }

    @GetMapping("/checkVehicleInUse/{vehiculoId}")
    public boolean checkVehicleInUse(@PathVariable Long vehiculoId) {
        // Check if the vehicle is being used in an active prueba
        return pruebaRepository.existsByVehiculoIdAndFechaHoraFinIsNull(vehiculoId);
    }

    @PutMapping("/terminarPrueba/{id}")
    public ResponseEntity<Prueba> terminarPrueba(@PathVariable Long id, @RequestBody Prueba pruebaDetails) {
        try{
            Prueba prueba = pruebaService.getPruebaById(id);
            if (prueba.getFechaHoraFin() == null) {
                prueba.setComentarios(pruebaDetails.getComentarios());
                prueba.setFechaHoraFin(LocalDateTime.now());
                return ResponseEntity.ok(pruebaService.save(prueba, false));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La prueba ingresada ya ha terminado");
            }

        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @GetMapping("active/{vehicleId}")
    public ResponseEntity<Prueba> getActivePruebaByVehicleId(@PathVariable Long vehicleId) {
        Prueba prueba = pruebaRepository.findActivePruebaByVehicleId(vehicleId);
        if (prueba == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // No active prueba found
        }
        return ResponseEntity.ok(prueba); // Return the active prueba
    }
}
