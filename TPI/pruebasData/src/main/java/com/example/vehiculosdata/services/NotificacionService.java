package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Notificacion;
import com.example.vehiculosdata.repositories.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotificacionService {
    private NotificacionRepository notificacionRepository;

    private final RestTemplate restTemplate;


    @Value("${vehiculos.service.url}")  // The URL of the Pruebas microservice
    private String vehiculosServiceUrl;

    @Autowired
    public NotificacionService(NotificacionRepository notificacionRepository, RestTemplate restTemplate) {
        this.notificacionRepository = notificacionRepository;
        this.restTemplate = restTemplate;
    }

    public Iterable<Notificacion> getNotificaciones(){
        return notificacionRepository.findAll();
    }

    public Notificacion saveNotificacion(Notificacion notificacion){
        String url = vehiculosServiceUrl + "/notificaciones";
        return notificacionRepository.save(notificacion);
    }

    public void deleteNotificacion(Notificacion notificacion){
        notificacionRepository.delete(notificacion);
    }

    public void sendNotifications(List<String> phoneNumbers, String message) {
        // Iterate over the phone numbers and create a Notificacion for each
        for (String phoneNumber : phoneNumbers) {
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje(message); // Set the same message for all notifications

            try {
                // Convert phone number from String to Long
                Long phoneLong = Long.parseLong(phoneNumber); // Make sure phone number is numeric
                notificacion.setNumero(phoneLong); // Set the phone number as Long
            } catch (NumberFormatException e) {
                // Handle invalid phone number format if needed
                // For now, set numero to null if invalid
                notificacion.setNumero(null);
            }

            notificacion.setInteresadoID(null); // Leave as null, not needed for this case
            notificacion.setEmpleadoID(null); // Leave as null, not needed for this case
            notificacion.setPosicionId(null); // Leave as null, not needed for this case

            notificacionRepository.save(notificacion); // Save the notification
        }
    }

}
