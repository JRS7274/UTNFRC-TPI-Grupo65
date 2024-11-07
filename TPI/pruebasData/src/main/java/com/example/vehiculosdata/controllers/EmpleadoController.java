package com.example.vehiculosdata.controllers;

import com.example.vehiculosdata.models.Empleado;
import com.example.vehiculosdata.models.Interesado;
import com.example.vehiculosdata.models.Prueba;
import com.example.vehiculosdata.services.EmpleadoService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping
    public ResponseEntity<?> getAllEmpleados() {
        Iterable<Empleado> empleados = empleadoService.findAll();

        if (!empleados.iterator().hasNext()) {
            return ResponseEntity.ok("No empleados found.");
        }

        return ResponseEntity.ok(empleados);
    }

    @GetMapping("{legajo}")
    public ResponseEntity<Empleado> getEmpleadoById(@PathVariable("legajo") Long legajo) {
        try{
            Empleado empleado = empleadoService.findById(legajo);
            return ResponseEntity.ok(empleado);
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @PostMapping
    public ResponseEntity<Empleado> addEmpleado(@RequestBody Empleado empleado) {
        return ResponseEntity.ok(empleadoService.save(empleado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> updateEmpleado(@PathVariable Long legajo, @RequestBody Empleado empleadoDetails) {
        try{
            Empleado empleado = empleadoService.findById(legajo);
            empleado.setNombre(empleadoDetails.getNombre());
            empleado.setApellido(empleadoDetails.getApellido());
            empleado.setTelefono_contacto(empleadoDetails.getTelefono_contacto());
            return ResponseEntity.ok(empleadoService.save(empleado));
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }

    @DeleteMapping("/{legajo}")
    public ResponseEntity deleteEmpleadoById(@PathVariable("legajo") Long legajo) {
        try{
            Empleado empleado = empleadoService.findById(legajo);
            empleadoService.delete(legajo);
            return ResponseEntity.ok("Empleado con legajo" + legajo + " borrado exitosamente.");
        } catch (ServiceException e) {
            return ResponseEntity.notFound().header("Error", e.getMessage()).build();
        }
    }
}
