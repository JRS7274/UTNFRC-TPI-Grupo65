package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Modelo;
import com.example.vehiculosdata.repositories.ModeloRepository;
import org.springframework.stereotype.Service;

@Service
public class ModeloService {
    private ModeloRepository modeloRepository;

    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    public Iterable<Modelo> getAllModelos() {
        return modeloRepository.findAll();
    }

    /*
    public Modelo getModeloByNombre(String nombre) {
        return modeloRepository.findByDescripcion(nombre);
    }

     */

    public Modelo getModeloById(Long id) {
        return modeloRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Modelo no encontrado"));
    }

    public Modelo createModelo(Modelo modelo) {
        return modeloRepository.save(modelo);
    }

    public Modelo updateModelo(Long id, Modelo modeloDetails) {
        Modelo modelo = modeloRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Modelo no encontrado"));

        modelo.setId(modeloDetails.getId());
        modelo.setDescripcion(modeloDetails.getDescripcion());
        return modeloRepository.save(modelo);
    }

    public void deleteModelo(Long id) {
        modeloRepository.deleteById(id);
    }
}
