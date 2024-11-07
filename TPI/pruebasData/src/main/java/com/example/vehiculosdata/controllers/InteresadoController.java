package com.example.vehiculosdata.controllers;

import com.example.vehiculosdata.models.Interesado;
import com.example.vehiculosdata.models.Prueba;
import com.example.vehiculosdata.services.InteresadoService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/interesados")
public class InteresadoController {

    private final InteresadoService interesadoService;

    @Autowired
    public InteresadoController(InteresadoService interesadoService) {
        this.interesadoService = interesadoService;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping
    public ResponseEntity<?> getAllInteresados() {
        Iterable<Interesado> interesados = interesadoService.getInteresados();

        if (!interesados.iterator().hasNext()) {
            return ResponseEntity.ok("No Interesados found.");
        }

        return ResponseEntity.ok(interesados);
    }

    @GetMapping("{id}")
    public ResponseEntity<Interesado> getInteresadoById(@PathVariable("id") Long id) {
        try {
            Interesado interesado = interesadoService.getInteresadoById(id);
            return ResponseEntity.ok(interesado);
        } catch (ServiceException e){
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @PostMapping
    public ResponseEntity<Interesado> addInteresado(@RequestBody Interesado interesado) {
        return ResponseEntity.ok(interesadoService.save(interesado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interesado> updateInteresado(@PathVariable("id") Long id, @RequestBody Interesado interesadoDetails) {
        try{
            Interesado interesado = interesadoService.getInteresadoById(id);
            interesado.setNombre(interesadoDetails.getNombre());
            interesado.setFecha_vencimiento_licencia(interesadoDetails.getFecha_vencimiento_licencia());
            interesado.setTipo_documento(interesadoDetails.getTipo_documento());
            interesado.setApellido(interesadoDetails.getApellido());
            interesado.setDocumento(interesadoDetails.getDocumento());
            interesado.setRestringido(interesadoDetails.getRestringido());
            interesado.setNro_licencia(interesadoDetails.getNro_licencia());
            return ResponseEntity.ok(interesadoService.save(interesado));
        } catch (ServiceException e){
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteInteresado(@PathVariable("id") Long id) {
        try{
            Interesado interesado = interesadoService.getInteresadoById(id);
            interesadoService.delete(interesado.getId());
            return ResponseEntity.ok("Interesado con ID " + id + " borrado exitosamente.");
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }
}
