package com.example.localisermonenfant_enfant.activity.Map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class areaMap {

    private static int color = 0x7F00FF00;

    private static int hoursStart;
    private static int minuteStart;

    private static int hoursEnd;
    private static int minutesEnd;

   private static ArrayList<LatLng> latLng = new ArrayList<>();

    public areaMap() {
    }

    public areaMap (int color, int hoursStart, int minuteStart, int hoursEnd, int minutesEnd) {

        this.color = color;
        this.hoursStart = hoursStart;
        this.minuteStart = minuteStart;
        this.hoursEnd = hoursEnd;
        this.minutesEnd = minutesEnd;
    }



    public static int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static int getHoursStart() {
        return hoursStart;
    }

    public void setHoursStart(int hoursStart) {
        this.hoursStart = hoursStart;
    }

    public static int getMinuteStart() {
        return minuteStart;
    }

    public void setMinuteStart(int minuteStart) {
        this.minuteStart = minuteStart;
    }

    public static int getHoursEnd() {
        return hoursEnd;
    }

    public void setHoursEnd(int hoursEnd) {
        this.hoursEnd = hoursEnd;
    }

    public static int getMinutesEnd() {
        return minutesEnd;
    }

    public void setMinutesEnd(int minutesEnd) {
        this.minutesEnd = minutesEnd;
    }

    public static ArrayList<LatLng> getLatLng() {
        return latLng;
    }

    public void setLatLng(ArrayList<LatLng> latLng) {
        this.latLng = latLng;
    }
}
