package com.example.localisermonenfant_enfant.activity.Contacts.CallLog;

import android.net.Uri;

public class CallLogModel {

    String phNumber;
    String callDayTime;
    String callDuration;
    String dir;

    public CallLogModel(String phNumber, String callDayTime, String callDuration, String dir) {
        this.phNumber = phNumber;
        this.callDayTime = callDayTime;
        this.callDuration = callDuration;
        this.dir = dir;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getCallDayTime() {
        return callDayTime;
    }

    public void setCallDayTime(String callDayTime) {
        this.callDayTime = callDayTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}