package com.example.localisermonenfant_enfant.activity.Map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class areaMap {

    private int color = 0x7F00FF00;

    private int hoursStart;
    private int minuteStart;

    private int hoursEnd;
    private int minutesEnd;

   private ArrayList<LatLng> latLng = new ArrayList<>();

    public areaMap() {
    }

    public areaMap (int color, int hoursStart, int minuteStart, int hoursEnd, int minutesEnd) {

        this.color = color;
        this.hoursStart = hoursStart;
        this.minuteStart = minuteStart;
        this.hoursEnd = hoursEnd;
        this.minutesEnd = minutesEnd;
    }



    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getHoursStart() {
        return hoursStart;
    }

    public void setHoursStart(int hoursStart) {
        this.hoursStart = hoursStart;
    }

    public int getMinuteStart() {
        return minuteStart;
    }

    public void setMinuteStart(int minuteStart) {
        this.minuteStart = minuteStart;
    }

    public int getHoursEnd() {
        return hoursEnd;
    }

    public void setHoursEnd(int hoursEnd) {
        this.hoursEnd = hoursEnd;
    }

    public int getMinutesEnd() {
        return minutesEnd;
    }

    public void setMinutesEnd(int minutesEnd) {
        this.minutesEnd = minutesEnd;
    }

    public ArrayList<LatLng> getLatLng() {
        return latLng;
    }

    public void setLatLng(ArrayList<LatLng> latLng) {
        this.latLng = latLng;
    }
}
