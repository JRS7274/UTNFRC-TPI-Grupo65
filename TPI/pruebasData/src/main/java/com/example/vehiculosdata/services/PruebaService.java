package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Interesado;
import com.example.vehiculosdata.models.Prueba;
import com.example.vehiculosdata.repositories.InteresadoRepository;
import com.example.vehiculosdata.repositories.PruebaRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PruebaService {
    private final InteresadoRepository interesadoRepository;
    private PruebaRepository pruebaRepository;

    private final RestTemplate restTemplate;

    @Value("${pruebas.service.url}")  // The URL of the Pruebas microservice
    private String pruebasServiceUrl;

    @Autowired
    public PruebaService(PruebaRepository pruebaRepository, InteresadoRepository interesadoRepository, RestTemplate restTemplate) { this.pruebaRepository = pruebaRepository;
        this.interesadoRepository = interesadoRepository;
        this.restTemplate = restTemplate;
    }

    public Iterable<Prueba> getAllPruebas() {
        return pruebaRepository.findAll();
    }

    public Iterable<Prueba> getPruebasEnCurso(){
        return pruebaRepository.findByFechaHoraFinIsNull();
    }

    public Prueba getPruebaById(Long id) {
        return pruebaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prueba no encontrada"));
    }

    public Boolean verificarCliente(@PathVariable Long interesadoId){
        //Encontrar interesado
        Interesado interesado = interesadoRepository
                .findById(interesadoId)
                .orElseThrow(() -> new ServiceException("Interesado no encontrado"));

        return !interesado.getRestringido() && isLicenciaValida(interesado.getFecha_vencimiento_licencia());
    }

    private Boolean isLicenciaValida(LocalDate fechaVencimientoLicencia){
        return fechaVencimientoLicencia != null && !fechaVencimientoLicencia.isBefore(LocalDate.now());
    }

    public boolean isVehiculoInUse(Long vehiculoId) {
        return pruebaRepository.existsByVehiculoIdAndFechaHoraFinIsNull(vehiculoId);
    }

    public Prueba save(Prueba pruebaDetails, Boolean flagCreate) {
        if (!flagCreate){
            return pruebaRepository.save(pruebaDetails);
        } else {
            // Check if the vehicle is already in use by calling the 'checkInUse' endpoint in Pruebas microservice
            String url = pruebasServiceUrl + "/api/pruebas/checkVehicleInUse/" + pruebaDetails.getVehiculoId();

            if (pruebaDetails.getVehiculoId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vehiculoId es requerido");
            }

            if (pruebaDetails.getFechaHoraInicio() == null || pruebaDetails.getFechaHoraFin() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La prueba no se puede crear con fecha de fin o sin fecha de inicio");
            }

            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            boolean isInUse = response.getBody() != null && response.getBody();
            System.out.println("Vehicle in use status: " + isInUse);

            if (isInUse) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El vehiculo ya está siendo usado en una prueba.");
            }

            if (verificarCliente(pruebaDetails.getInteresadoId())) {
                return pruebaRepository.save(pruebaDetails);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El cliente está restringido o su licencia es inválida.");
            }
        }
    }

    public Prueba terminarPrueba(Long id, Prueba pruebaDetails){
        Prueba prueba = pruebaRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Prueba no encontrada"));
        prueba.setFechaHoraFin(LocalDateTime.now());
        prueba.setComentarios(pruebaDetails.getComentarios());
        return pruebaRepository.save(prueba);
    }
}
