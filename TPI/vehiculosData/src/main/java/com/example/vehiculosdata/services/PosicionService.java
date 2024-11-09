package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Posicion;
import com.example.vehiculosdata.models.Vehiculo;
import com.example.vehiculosdata.repositories.ModeloRepository;
import com.example.vehiculosdata.repositories.PosicionRepository;
import com.example.vehiculosdata.repositories.VehiculoRepository;
import org.springframework.stereotype.Service;

@Service
public class PosicionService {
    private final ModeloRepository modeloRepository;
    private final VehiculoRepository vehiculoRepository;
    private PosicionRepository posicionRepository;

    public PosicionService(PosicionRepository posicionRepository, ModeloRepository modeloRepository, VehiculoRepository vehiculoRepository) {
        this.posicionRepository = posicionRepository;
        this.modeloRepository = modeloRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    public Iterable<Posicion> getPosiciones(){
        return posicionRepository.findAll();
    }

    public Posicion getPosicionById(Long id){
        return posicionRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Posicion no encontrada"));
    }

    public Posicion createPosicion(Posicion posicion){
        Vehiculo vehiculo = vehiculoRepository
                .findById(posicion.getVehiculo()
                        .getId())
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        posicion.setVehiculo(vehiculo);

        return posicionRepository.save(posicion);
    }

    public void deletePosicion(Posicion posicion){
        posicionRepository.delete(posicion);
    }
}
