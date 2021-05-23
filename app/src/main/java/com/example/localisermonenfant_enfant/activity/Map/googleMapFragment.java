package com.example.localisermonenfant_enfant.activity.Map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;
import com.example.localisermonenfant_enfant.activity.MainMenu.SettingsActivity;
import com.example.localisermonenfant_enfant.activity.SendDataChild.SendDataChildActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.localisermonenfant_enfant.activity.MainMenu.SettingsActivity.notificationType;


public class googleMapFragment extends Fragment implements LocationListener {

    public static final int PERMS_MAP_ID = 1234;
    private LocationManager lm;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private Marker positionChild;
    private double lo = 0;
    private double la = 0;
    private LatLng lastPosition;

    public googleMapFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log_in.c.GetGPS(getContext(), MainMenu.child, new Connection.GetGPSCallback() {
            @Override
            public void OnSuccess(double lat, double lon) {
                lo = lon;
                la = lat;
                checkPermission();
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

            }


            @Override
            public void OnError() {

            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_MAP_ID);
            return;
        }

        lm = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }

        if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0, this);
        }

        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
        }
        loadMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_MAP_ID) {
            checkPermission();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lm != null) {
            lm.removeUpdates(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void loadMap() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMapFragment.this.googleMap = googleMap;

                googleMap.setMyLocationEnabled(true);
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(la, lo)).title(MainMenu.child.getName());
                googleMap.addMarker(markerOptions);

                positionChild = googleMap.addMarker(markerOptions);
                LatLng googleLocation = new LatLng(la, lo);
                googleMap.moveCamera(CameraUpdateFactory.zoomBy(30));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
                Polyline Polyline = null;
                PolylineOptions path = new PolylineOptions().clickable(true);
                for (LatLng coord : SendDataChildActivity.hashMap.get(MainMenu.child.getId())) {
                    path.add(coord);


                    Polyline = googleMap.addPolyline(path);
                }


                Polyline.setColor(0x7FFF2B00);

                for (areaMap areaMap : MapActivity.areaList) {
                    PolygonOptions polygonOptions = new PolygonOptions().clickable(true);
                    for (LatLng coord : areaMap.getLatLng()) {
                        polygonOptions.add(coord);
                        Polygon polygon = googleMap.addPolygon(polygonOptions);
                        polygon.setStrokeColor(areaMap.getColor());
                        polygon.setFillColor(areaMap.getColor());
                    }
                    boolean contains = PolyUtil.containsLocation(lastPosition, areaMap.getLatLng(), true);

                    Calendar rightNow = Calendar.getInstance();
                    int hours = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
                    int minutes = rightNow.get(Calendar.MINUTE);
                    Time currentTime = new Time(hours, minutes, 00);
                    Time limitTimeStart = new Time(areaMap.getHoursStart(), areaMap.getMinuteStart(), 00);
                    Time limitTimeEnd = new Time(areaMap.getHoursEnd(), areaMap.getMinutesEnd(), 00);
                    if (notificationType.equals("Pop up")) {
                    if ((areaMap.getColor() == 0x7FFF7F00 || areaMap.getColor() == 0x7FFF2B00) && contains && limitTimeStart.before(currentTime) && limitTimeEnd.after(currentTime)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setCancelable(true);
                            builder.setTitle("Alerte");
                            builder.setMessage(MainMenu.child.getName() + " is in " + (areaMap.getColor() == 0x7FFF2B00 ? "red" : "orange ") + " area!");
                            builder.setPositiveButton("Oui",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Log_in.c.Pa
                                        }
                                    });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();


                        } else if (areaMap.getColor() == 0x7F00FF00 && !contains && limitTimeStart.before(currentTime) && limitTimeEnd.after(currentTime)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setCancelable(true);
                            builder.setTitle("Alerte");
                            builder.setMessage(MainMenu.child.getName() + " is out of green area!");
                            builder.setPositiveButton("Oui",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Log_in.c.Pa
                                        }
                                    });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    else if (notificationType.equals("Vibration")) {
                            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                v.vibrate(500);
                            }
                        } else {

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "Channel ID")
                                    .setSmallIcon(R.drawable.ic_baseline_warning_24)
                                    .setContentTitle("Alerte")
                                    .setContentText(MainMenu.child.getName() + " is in a bad place")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                            notificationManager.notify(10, builder.build());
                        }


                }


                final ArrayList<LatLng> pointsList = new ArrayList<>();
                final ArrayList<Marker> markerList = new ArrayList<>();
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if (MapActivity.addZone) {
                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude));
                            Marker area = googleMap.addMarker(markerOptions);
                            markerList.add(area);
                            MapActivity.areaList.get(MapActivity.areaList.size() - 1).getLatLng().add(latLng);
                            pointsList.add(latLng);

                        } else if (!pointsList.isEmpty()) {
                            int color = MapActivity.areaList.get(MapActivity.areaList.size() - 1).getColor();
                            PolygonOptions polygonOptions = new PolygonOptions().clickable(true);
                            for (LatLng coord : pointsList) {
                                polygonOptions.add(coord);
                            }
                            Polygon polygon1 = googleMap.addPolygon(polygonOptions);
                            polygon1.setStrokeColor(color);
                            polygon1.setFillColor(color);
                            for (Marker marker : markerList) {
                                marker.remove();
                            }
                            pointsList.clear();
                            markerList.clear();
                        }
                    }
                });


                // Add polygons to indicate areas on the map.


// Store a data object with the polygon, used here to indicate an arbitrary type.


//                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(43.630988, 3.861163)));
//                animateMarker(marker,new LatLng(43.630988, 3.861163), false);
            }

        });

    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

//        Toast.makeText(getContext(), " Location " + latitude + "/" + longitude,Toast.LENGTH_LONG).show();
        if (googleMap != null && !Log_in.c.GetConnectionType().toString().equals("Parent")) {
            LatLng googleLocation = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));

        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
