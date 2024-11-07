package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Interesado;
import com.example.vehiculosdata.repositories.InteresadoRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class InteresadoService {
    private InteresadoRepository interesadoRepository;

    public InteresadoService(InteresadoRepository interesadoRepository) {
        this.interesadoRepository = interesadoRepository;
    }

    public Iterable<Interesado> getInteresados() {
        return interesadoRepository.findAll();
    }

    public Interesado getInteresadoById(Long id) {
        return interesadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontro el interesado"));
    }

    public Interesado save(Interesado interesado) {
        return interesadoRepository.save(interesado);
    }

    public Interesado update(Long id, Interesado interesadoDetails) throws ServiceException {
        Interesado interesado = interesadoRepository.findById(id).orElseThrow(()
                -> new RuntimeException("No se encontro el interesado"));
        interesado.setNombre(interesadoDetails.getNombre());
        interesado.setApellido(interesadoDetails.getApellido());
        interesado.setDocumento(interesadoDetails.getDocumento());
        interesado.setNro_licencia(interesadoDetails.getNro_licencia());
        interesado.setTipo_documento(interesadoDetails.getTipo_documento());
        interesado.setRestringido(interesadoDetails.getRestringido());
        interesado.setFecha_vencimiento_licencia(interesadoDetails.getFecha_vencimiento_licencia());
        return interesadoRepository.save(interesado);
    }

    public void delete(Long id) throws ServiceException {
        interesadoRepository.deleteById(id);
    }
}
