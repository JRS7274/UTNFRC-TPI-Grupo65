package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Vehiculo;
import com.example.vehiculosdata.repositories.VehiculoRepository;
import org.springframework.stereotype.Service;

@Service
public class VehiculoService {
    private VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public Iterable<Vehiculo> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    public Vehiculo getVehiculoById(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));
    }

    public Vehiculo save(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    public Vehiculo update(Long id, Vehiculo vehiculoDetails) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));
        vehiculo.setAnio(vehiculoDetails.getAnio());
        vehiculo.setModelo(vehiculoDetails.getModelo());
        vehiculo.setPatente(vehiculoDetails.getPatente());
        return vehiculoRepository.save(vehiculo);
    }

    public void delete(Long id) {
        vehiculoRepository.deleteById(id);
    }

}
