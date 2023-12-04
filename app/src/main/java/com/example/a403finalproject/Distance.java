package com.example.a403finalproject;

public class Distance {
    private static final double EARTH_RADIUS = 6371; // Earth radius in kilometers
    public double calculateDistance(
            double lat1, double lon1,
            double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceInKm = EARTH_RADIUS * c;
        // Convert distance from kilometers to miles
        double distanceInMiles = distanceInKm * 0.621371;
        return distanceInMiles;
    }
}
