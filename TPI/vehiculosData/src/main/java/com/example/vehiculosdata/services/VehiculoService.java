package com.example.vehiculosdata.services;

import com.example.vehiculosdata.config.LimitConfig;
import com.example.vehiculosdata.models.Posicion;
import com.example.vehiculosdata.models.PruebaDTO;
import com.example.vehiculosdata.models.Vehiculo;
import com.example.vehiculosdata.models.Zona;
import com.example.vehiculosdata.repositories.PosicionRepository;
import com.example.vehiculosdata.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VehiculoService {
    private VehiculoRepository vehiculoRepository;
    private final LimitesService limitService;
    private final PosicionRepository posicionRepository;

    private final String pruebaUrl = "http://localhost:8085/api/pruebas/active/"; // URL for fetching prueba by vehicleId
    private final String notificacionUrl = "http://localhost:8085/api/pruebas/notificaciones"; // URL for creating notification


    @Autowired
    private RestTemplate restTemplate;

    public VehiculoService(VehiculoRepository vehiculoRepository, LimitesService limitService, PosicionRepository posicionRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.limitService = limitService;
        this.posicionRepository = posicionRepository;
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

    public PruebaDTO getActivePrueba(Long vehiculoId) {
        String url = pruebaUrl + vehiculoId;
        return restTemplate.getForObject(url, PruebaDTO.class);
    }

    public boolean isVehicleInLimits(Long vehicleId) {
        // First, check if the vehicle is currently in a test drive
        boolean isInTestDrive = isInTestDrive(vehicleId);
        if (!isInTestDrive) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Vehicle is not in a test drive");
        }

        // Fetch all positions for the vehicle ordered by fechaHora descending
        List<Posicion> posiciones = posicionRepository.findPositionsForVehicle(vehicleId);
        if (posiciones.isEmpty()) {
            return false; // No position data available for this vehicle
        }

        // Get the latest position (first item in the list)
        Posicion posicion = posiciones.get(0);

        // Fetch the limit configuration from LimitService
        LimitConfig config = limitService.getLimitConfig();
        System.out.println("Vehicle Position: Lat=" + posicion.getLat() + ", Lon=" + posicion.getLon());
        System.out.println("Limit Config: Radius=" + config.getRadioAdmitidoKm());
        System.out.println("Restricted Zones: " + config.getZonasRestringidas());

        // Check if the position is within the allowed radius
        boolean withinRadius = checkIfWithinRadius(posicion, config);
        System.out.println("Within Radius: " + withinRadius);

        // Check if the position is inside any restricted zones using the isInZone method
        boolean withinRestrictedZone = false;
        for (Zona zona : config.getZonasRestringidas()) {
            if (isInZone(posicion, zona)) {
                withinRestrictedZone = true;
                System.out.println("Vehicle is inside restricted zone: " + zona);
                break; // Exit early if the vehicle is inside one restricted zone
            }
        }
        System.out.println("Within Restricted Zone: " + withinRestrictedZone);

        boolean result = withinRadius && !withinRestrictedZone;
        System.out.println("Final Result for Vehicle ID " + vehicleId + ": " + result);


        if (!result) {
            // Create test values for the Notificacion

            Map<String, Object> notificationData = new HashMap<>();
            Long posicionId = posicion.getId();
            String mensaje = "Vehicle is outside allowed limits";

            notificationData.put("posicionId", 2); // Test position ID
            notificationData.put("mensaje", "Vehicle is out of allowed limits.");
            notificationData.put("empleado_id", 1); // Test employee ID
            notificationData.put("interesado_id", 1); // Test interested party ID
            notificationData.put("numero", 1234567890); // Test phone number

            // Use RestTemplate to call the notification endpoint
            String notificacionUrl = "http://localhost:8085/api/pruebas/notificaciones"; // Adjust to the actual URL
            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData);

            try {
                ResponseEntity<Void> response = restTemplate.exchange(
                        notificacionUrl,
                        HttpMethod.POST,
                        request,
                        Void.class
                );
                System.out.println("Notification created: " + response.getStatusCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result; // Return true if within radius and not in restricted zone
    }

    private boolean checkIfWithinRadius(Posicion posicion, LimitConfig config) {
        double distance = calculateDistance(posicion.getLat(), posicion.getLon(),
                config.getCoordenadasAgencia().getLat(),
                config.getCoordenadasAgencia().getLon());

        return distance <= config.getRadioAdmitidoKm();
    }

    private boolean checkIfWithinRestrictedZones(Posicion posicion, LimitConfig config) {
        for (Zona zone : config.getZonasRestringidas()) {
            if (isInZone(posicion, zone)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInZone(Posicion posicion, Zona zone) {
        double latMin = Math.min(zone.getNoroeste().getLat(), zone.getSureste().getLat());
        double latMax = Math.max(zone.getNoroeste().getLat(), zone.getSureste().getLat());

        double lonMin = Math.min(zone.getNoroeste().getLon(), zone.getSureste().getLon());
        double lonMax = Math.max(zone.getNoroeste().getLon(), zone.getSureste().getLon());

        double vehicleLat = posicion.getLat();
        double vehicleLon = posicion.getLon();

        // Debug log to verify all values
        System.out.println("latMin: " + latMin + ", latMax: " + latMax);
        System.out.println("lonMin: " + lonMin + ", lonMax: " + lonMax);
        System.out.println("vehicleLat: " + vehicleLat + ", vehicleLon: " + vehicleLon);

        // Check if vehicle's position is within the zone's boundaries
        boolean isInside = (vehicleLat >= latMin && vehicleLat <= latMax)
                && (vehicleLon >= lonMin && vehicleLon <= lonMax);

        // Debug output to verify the result of the condition
        System.out.println("Is vehicle inside zone? " + isInside);

        return isInside;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Euclidean distance calculation
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }

    public boolean isInTestDrive(Long vehiculoId) {
        // Make a request to the endpoint to check if the vehicle is in a test drive
        // Example using RestTemplate:
        String url = "http://localhost:8081/api/pruebas/vehiculo/" + vehiculoId;
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class);
        return response.getBody();
    }
}
