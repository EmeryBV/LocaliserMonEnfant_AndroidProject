package com.example.localisermonenfant_enfant.activity.Map;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;
import com.example.localisermonenfant_enfant.activity.SendDataChild.SendDataChildActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.localisermonenfant_enfant.activity.MainMenu.SettingsActivity.notificationType;
import static com.example.localisermonenfant_enfant.activity.Map.googleMapFragment.googleMap;
import static com.example.localisermonenfant_enfant.activity.Map.googleMapFragment.lastPosition;
import static com.example.localisermonenfant_enfant.activity.Map.googleMapFragment.positionChild;
import static java.lang.Thread.sleep;

public class LocalisationChild extends Service {
    public Handler handler = null;
    public static Runnable runnable = null;


    public LocalisationChild() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {

                handler.postDelayed(runnable, 15000);
                Log.e("DEBUG", "Récuperation de la position");
                Log_in.c.GetGPS(getApplicationContext(), MainMenu.child, new Connection.GetGPSCallback() {
                    @Override
                    public void OnSuccess(double lat, double lon) {
                        googleMapFragment.lo = lon;
                        googleMapFragment.la = lat;
                        ArrayList<LatLng> latLngs = SendDataChildActivity.hashMap.get(MainMenu.child.getId());
                        if (!(latLngs == null)) {

                            LatLng latLng = new LatLng(lat, lon);
                            latLngs.add(latLng);
                            SendDataChildActivity.hashMap.put(MainMenu.child.getId(), latLngs);
                            lastPosition = latLng;
                        } else {
                            LatLng latLng = new LatLng(lat, lon);
                            latLngs = new ArrayList<>();
                            latLngs.add(latLng);
                            for (int i = 0; i < latLngs.size(); i++) {
                            }
                            SendDataChildActivity.hashMap.put(MainMenu.child.getId(), latLngs);
                            lastPosition = latLng;
                        }
                        if(googleMap!=null){
                            if(positionChild!=null)positionChild.remove();
                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lon)).title(MainMenu.child.getName());
                            googleMapFragment.positionChild = googleMap.addMarker(markerOptions);
                            Polyline Polyline = null;
                            PolylineOptions path = new PolylineOptions().clickable(true);
                            for (LatLng coord : SendDataChildActivity.hashMap.get(MainMenu.child.getId())) {
                                path.add(coord);
                                Polyline = googleMap.addPolyline(path);
                            }

                            Polyline.setColor(0x7F000000);
                            boolean contains = PolyUtil.containsLocation(positionChild.getPosition(), areaMap.getLatLng(), true);

                            Calendar rightNow = Calendar.getInstance();
                            int hours = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
                            int minutes = rightNow.get(Calendar.MINUTE);
                            Time currentTime = new Time(hours, minutes, 00);
                            Time limitTimeStart = new Time(areaMap.getHoursStart(), areaMap.getMinuteStart(), 00);
                            Time limitTimeEnd = new Time(areaMap.getHoursEnd(), areaMap.getMinutesEnd(), 00);
                            if ((areaMap.getColor() == 0x7FFF7F00 || areaMap.getColor() == 0x7FFF2B00) && contains && limitTimeStart.before(currentTime) && limitTimeEnd.after(currentTime)
                            ||(areaMap.getColor() == 0x7F00FF00 && !contains && limitTimeStart.before(currentTime) && limitTimeEnd.after(currentTime))
                            ) {
                                if (notificationType.equals("Vibration")) {
                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                    } else {
                                        v.vibrate(500);
                                    }
                                } else {
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Channel ID")
                                            .setSmallIcon(R.drawable.ic_baseline_warning_24)
                                            .setContentTitle("Alerte")
                                            .setContentText(MainMenu.child.getName() + " is in a bad place")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setAutoCancel(true);
                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                    notificationManager.notify(10, builder.build());
                                }
                            }
                        }

                    }

                    @Override
                    public void OnError() {

                    }
                });
            }

        };
        handler.postDelayed(runnable, 10000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}