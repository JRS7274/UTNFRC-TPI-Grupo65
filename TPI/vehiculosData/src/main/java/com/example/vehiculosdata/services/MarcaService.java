package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Marca;
import com.example.vehiculosdata.repositories.MarcaRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarcaService {
    private MarcaRepository marcaRepository;

    @Autowired
    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public Iterable<Marca> getMarcas() {
        return marcaRepository.findAll();
    }

    public Marca getMarcaByNombre(String nombre) {
        return marcaRepository.findByNombre(nombre);
    }

    public Marca getById(Long id) {
        return marcaRepository.findById(id).orElseThrow(() ->
                new RuntimeException("No se encontro la marca"));
    }

    public Marca create(Marca marca) {
        return marcaRepository.save(marca);
    }

    public Marca update(Long id, Marca marcaDetails) throws ServiceException {
        Marca marca = marcaRepository.findById(id).orElseThrow(()
                -> new RuntimeException("No se encontro la marca"));

        marca.setId(marcaDetails.getId());
        marca.setNombre(marcaDetails.getNombre());
        return marcaRepository.save(marca);
    }

    public void delete(Long id) {
        marcaRepository.deleteById(id);
    }

}
