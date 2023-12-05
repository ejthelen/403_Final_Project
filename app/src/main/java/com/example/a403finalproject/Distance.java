package com.example.a403finalproject;

import java.text.DecimalFormat;

public class Distance {
    private static final double EARTH_RADIUS = 6371; // Earth radius in kilometers
    public static double calculateDistance(
            double lat1, double lon1,
            double lat2, double lon2) {
        DecimalFormat df = new DecimalFormat("#.#");

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceInMiles = EARTH_RADIUS * c;
        double roundedValue = Double.parseDouble(df.format(distanceInMiles));
        return roundedValue;
    }
}
