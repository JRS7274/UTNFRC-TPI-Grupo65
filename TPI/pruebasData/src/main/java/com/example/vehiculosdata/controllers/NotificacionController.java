package com.example.vehiculosdata.controllers;

import com.example.vehiculosdata.models.Notificacion;
import com.example.vehiculosdata.services.NotificacionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pruebas/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @Autowired
    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping
    public ResponseEntity<Iterable<Notificacion>> getNotificacion() {
        Iterable<Notificacion> notificacion = notificacionService.getNotificaciones();
        return ResponseEntity.ok(notificacion);

    }

    @PostMapping
    public ResponseEntity<Notificacion> addNotificacion(@RequestBody Notificacion notificacion) {
        return ResponseEntity.ok(notificacionService.saveNotificacion(notificacion));
    }

    @PostMapping("/sendBulkNotifications")
    public ResponseEntity<String> sendBulkNotifications(@RequestBody SendNotificationsRequest request) {
        // Call service method to send notifications
        notificacionService.sendNotifications(request.getPhoneNumbers(), request.getMessage());
        return ResponseEntity.ok("Notifications sent successfully");
    }

    public static class SendNotificationsRequest {
        private List<String> phoneNumbers; // List of phone numbers to send notifications to
        private String message;            // The message to send

        // Getters and setters
        public List<String> getPhoneNumbers() {
            return phoneNumbers;
        }

        public void setPhoneNumbers(List<String> phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
