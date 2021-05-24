package com.example.localisermonenfant_enfant.Service;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CallLog;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.Contacts.CallLog.CallLogActivity;
import com.example.localisermonenfant_enfant.activity.Contacts.CallLog.CallLogModel;
import com.example.localisermonenfant_enfant.activity.Contacts.Contacts;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.Sms;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.SmsActivity;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;
import com.example.localisermonenfant_enfant.activity.MainMenu.SettingsActivity;
import com.example.localisermonenfant_enfant.activity.Media.Picture.ImagesGallery;
import com.example.localisermonenfant_enfant.activity.SendDataChild.SendDataChildActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import static com.example.localisermonenfant_enfant.activity.Contacts.SMS.SmsActivity.getAllSms;
import static com.example.localisermonenfant_enfant.activity.SendDataChild.SendDataChildActivity.contactsArrayList;
import static java.lang.Thread.sleep;

public class SendDataService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    NotificationManagerCompat notificationManager;
    final int KEEPUS_NOTIFICATION_ID = 1;
    private FusedLocationProviderClient FusedLocationClient;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private Long dateLastUpdate = (long) 0;
    private static final String TAG = "TAG";
    public static Location location = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "ChannelService ID")
                .setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setContentTitle("Alerte")
                .setContentText("Envoie des données")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(10, builder.build());

        FusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initializeLocationManager();

//        while (true) {
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);


            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }

            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            } else {
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }


            double latitude = 0;
            double longitude = 0;
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }


        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        Log_in.c.SendImages(getApplicationContext(), SendDataChildActivity.images, new Connection.SendImagesCallback() {
            @Override
            public void OnSuccess() {
                Log.e("Debug", "Image envoyé !  ");
            }

            @Override
            public void OnError() {
                Log.e("Debug", "Erreur lors de l'envoies des images: ");
            }
        });

        Log_in.c.SendVideos(getApplicationContext(), SendDataChildActivity.videoArrayList, new Connection.SendVideosCallback() {
            @Override
            public void OnSuccess() {
                Log.e("Debug", "Vidéo envoyé !  ");
            }

            @Override
            public void OnError() {
                Log.e("Debug", "Erreur lors de l'envoies des images: ");
            }
        });


        Log_in.c.GetSMSLastDateCallback(getApplicationContext(), new Connection.GetSMSLastDateCallback() {
            @Override
            public void OnSuccess(Long date) {

                dateLastUpdate = date;

            }

            @Override
            public void OnError() {
//                            Log.e("Debug", "Erreur lors de l'envoie de la derniere date ");
            }
        });
        for (Contacts contact : contactsArrayList) {
            SmsActivity.contact = new Connection.Contact(0, contact.getName(), contact.getNumber());
            SmsActivity.getAllSms(getApplicationContext());
            ArrayList<Connection.SMS> smsSend = new ArrayList<>();
            for (Sms sms : SmsActivity.listSms) {
                if (sms.getType() != null && Long.parseLong(sms.getCreatedAt())>dateLastUpdate) {
                    Connection.SMS sms1 = new Connection.SMS(0, null, SmsActivity.contact, sms.getMessage(), Long.parseLong(sms.getCreatedAt()),
                            sms.getType().equals("sent"));
                    smsSend.add(sms1);
                }
            }

            Log_in.c.SendSMS(getApplicationContext(), smsSend, new Connection.SendSMSCallback() {
                @Override
                public void Success() {
                    Log.e("Debug", "SMS envoyé !  ");
                }

                @Override
                public void Error() {
                    Log.e("Debug", "Erreur lors de l'envoie des sms ");
                }
            });

            CallLogActivity.getCallDetails(contact.getNumber(),getApplicationContext());
            ArrayList<Connection.CallData> callLogSend = new ArrayList<>();
            for (CallLogModel callLog : SendDataChildActivity.callLogList) {
                Connection.Contact contact1 = new Connection.Contact(0,contact.getName(),contact.getNumber());
                int type = 0;
                if(callLog.getDir()!=null&& Long.parseLong(callLog.getCallDayTime())>dateLastUpdate){
                type = callLog.getDir().equals(getString(R.string.OutGoing))? 2 : callLog.getDir().equals(getString(R.string.Incoming))? 1 : 3;
                }
//                Log.e("Debuuuuug", callLog.getCallDuration());
                        Connection.CallData callData = new Connection.CallData(0,contact1, null,Long.parseLong(callLog.getCallDayTime()),type,callLog.getCallDuration());
                callLogSend.add(callData);
            }
            Log_in.c.SendCallData(getApplicationContext(), callLogSend, new Connection.SendCallDataCallback() {
                @Override
                public void OnSuccess() {

                        Log.e("Debug", "callLogs envoyé !  ");
                }

                @Override
                public void OnError() {
                    Log.e("Debug", "Erreur lors de l'envoie des callLogs ");
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        Log.e("Debug", "Service detruit");
        notificationManager.cancel(KEEPUS_NOTIFICATION_ID);
        super.onDestroy();
    }

    public SendDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);


        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            Log_in.c.SetGPS(getApplicationContext(), location.getLongitude(), location.getLatitude(), new Connection.SetGPSCallback() {
                @Override
                public void OnSuccess() {
                    try {
                        sleep(5000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnError() {
                    Log.e("Erreur", "Erreur lors de l'envoie de la postion de l'enfant : ");
                }
            });

        }


        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }

    }
}