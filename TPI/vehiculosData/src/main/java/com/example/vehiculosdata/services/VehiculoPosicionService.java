package com.example.vehiculosdata.services;

import com.example.vehiculosdata.models.Posicion;
import com.example.vehiculosdata.models.Zona;
import com.example.vehiculosdata.repositories.PosicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculoPosicionService {
    @Autowired
    private PosicionRepository posicionRepository;
    @Autowired
    private VehiculoPruebaService vehiculoPruebaService;

    // Method to retrieve the most recent position of a vehicle
    public Posicion getCurrentPosition(Long vehiculoId) {
        // Fetch all positions for the vehicle ordered by fechaHora descending
        List<Posicion> posiciones = posicionRepository.findPositionsForVehicle(vehiculoId);

        // Check if the list is empty, meaning no positions were found for this vehicle
        if (posiciones.isEmpty()) {
            return null; // No position data available for this vehicle
        }

        // Return the most recent position (first item in the list)
        return posiciones.get(0);
    }

    // Calculate the Euclidean distance between two coordinates (lat1, lon1) and (lat2, lon2)
    public double calculateEuclideanDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDiff = lat2 - lat1;
        double lonDiff = lon2 - lon1;
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);  // Pythagorean theorem
    }

    public double convertToKilometers(double latDiff, double lonDiff) {
        double latKm = latDiff * 111.0;  // Approximate km per degree of latitude
        double lonKm = lonDiff * 111.0;  // Approximate km per degree of longitude
        return Math.sqrt(latKm * latKm + lonKm * lonKm);
    }

    public boolean isWithinRadius(double vehicleLat, double vehicleLon, double agencyLat, double agencyLon, double radioAdmitidoKm) {
        double latDiff = vehicleLat - agencyLat;
        double lonDiff = vehicleLon - agencyLon;

        double distance = convertToKilometers(latDiff, lonDiff);
        return distance <= radioAdmitidoKm;  // Check if within the allowed radius
    }

    public boolean isInRestrictedZone(double vehicleLat, double vehicleLon, List<Zona> restrictedZones) {
        for (Zona zone : restrictedZones) {
            double latMin = Math.min(zone.getNoroeste().getLat(), zone.getSureste().getLat());
            double latMax = Math.max(zone.getNoroeste().getLat(), zone.getSureste().getLat());
            double lonMin = Math.min(zone.getNoroeste().getLon(), zone.getSureste().getLon());
            double lonMax = Math.max(zone.getNoroeste().getLon(), zone.getSureste().getLon());

            // Check if the vehicle's position is within the bounds of the zone
            if (vehicleLat >= latMin && vehicleLat <= latMax && vehicleLon >= lonMin && vehicleLon <= lonMax) {
                return true;  // Vehicle is inside a restricted zone
            }
        }
        return false;  // Vehicle is not inside any restricted zone
    }
}
