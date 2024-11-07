package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Empleado;
import com.example.vehiculosdata.repositories.EmpleadoRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {
    private EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Iterable<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    public Empleado findById(Long id) {
        return empleadoRepository.findById(id).orElseThrow(() ->
                new RuntimeException("No se encontro el id de la empleado"));
    }

    public Empleado save(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public Empleado update(Long id, Empleado empleadoDetails) throws ServiceException {
        Empleado empleado = empleadoRepository.findById(id).orElseThrow(()
                -> new RuntimeException("No se encontro el interesado"));
        empleado.setNombre(empleadoDetails.getNombre());
        empleado.setApellido(empleadoDetails.getApellido());
        empleado.setTelefono_contacto(empleadoDetails.getTelefono_contacto());
        return empleadoRepository.save(empleado);
    }

    public void delete(Long id) throws ServiceException {
        empleadoRepository.deleteById(id);
    }
}
