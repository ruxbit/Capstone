package com.ruxbit.bikecompanion.utils;

public class UnitConverter {
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int METERS_IN_KM = 1000;

    public static float secondsToHours(float seconds) {
        return seconds / SECONDS_IN_HOUR;
    }
    public static float hoursToSeconds(float hours) {
        return hours * SECONDS_IN_HOUR;
    }
    public static int hoursToSeconds(int hours) { return hours * SECONDS_IN_HOUR;}
    public static float metersToKm(float meters) {
        return meters / METERS_IN_KM;
    }
    public static int metersToKm(int meters) {
        return meters / METERS_IN_KM;
    }
    public static float kmToMeters(float km) {
        return km * METERS_IN_KM;
    }
    public static int kmToMeters(int km) {
        return km * METERS_IN_KM;
    }
}
