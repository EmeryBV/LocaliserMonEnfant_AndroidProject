package com.example.localisermonenfant_enfant.activity.SendDataChild;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.Contacts.CallLog.CallLogAdapter;
import com.example.localisermonenfant_enfant.activity.Contacts.CallLog.CallLogModel;
import com.example.localisermonenfant_enfant.activity.Contacts.Contacts;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.Sms;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.SmsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.example.localisermonenfant_enfant.activity.Contacts.SMS.SmsActivity.getAllSms;

public class SendDataChildActivity extends AppCompatActivity {
    private String TAG = "TAG";
    private static final int PERMS_CONTACT_ID = 1235;
    private static final int MY_READ_PERMISSION_CODE = 105;
    public static final int PERMS_MAP_ID = 1234;
    private int REQUEST_PHONE_CALL = 1237;
    public static HashMap<Integer, ArrayList<LatLng>> hashMap= new HashMap<>();
    Button addParent;
    LinearLayout linearLayout;
    TextView nameParent;
    private LocationManager lm;
    public ArrayList<Contacts> contactsArrayList = new ArrayList<>();
    ArrayList<CallLogModel> callLogList = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data_child);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        addParent = findViewById(R.id.addParentButton);
        linearLayout = findViewById(R.id.linearLayout);
        nameParent = findViewById(R.id.nameParent);

        Log_in.c.GetParent(getApplicationContext(), new Connection.GetParentCallback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void OnSuccess(final Connection.Parent parent) {

                if (parent == null) {
                    addParent.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    addParent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplication(), Log_in.class);
                            intent.putExtra("fromChild", true);
                            startActivity(intent);
                        }
                    });
                } else {
                    nameParent.setText(parent.getName());
                    checkContactPermissions();

                    checkMapPermission();
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.i(TAG, "Je suis la : ");
                        return;
                    }

                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(SendDataChildActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    ;
                                    if (location != null) {
                                        Log_in.c.SetGPS(getApplicationContext(), location.getLongitude(), location.getLatitude(), new Connection.SetGPSCallback() {
                                            @Override
                                            public void OnSuccess() {
                                                Log.e("Debug", "Position de l'enfant envoyé : ");
                                            }

                                            @Override
                                            public void OnError() {
                                                Log.e("Erreur", "Erreur lors de l'envoie de la postion de l'enfant : ");
                                            }
                                        });
                                    }
                                }
                            });

                    for (Contacts contact : contactsArrayList) {
                        SmsActivity.contact = new Connection.Contact(0, contact.getName(), contact.getNumber());
                        checkSMSPermissions();

                        ArrayList<Connection.SMS> smsSend = new ArrayList<>();
                        for (Sms sms : SmsActivity.listSms) {
                            if (sms.getType() != null) {
                                Connection.SMS sms1 = new Connection.SMS(0, null, SmsActivity.contact, sms.getMessage(), sms.getCreatedAt(),
                                        sms.getType().equals("sent"));
                                smsSend.add(sms1);
                            }
                        }

                        Log_in.c.SendSMS(getApplicationContext(), smsSend, new Connection.SendSMSCallback() {
                            @Override
                            public void Success() {
                                Toast.makeText(getApplicationContext(), "Sms envoyés !", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void Error() {
                                Toast.makeText(getApplicationContext(), "Erreur lors de l'envoie des SMS", Toast.LENGTH_LONG).show();
                            }
                        });
                        checkCallLogPermissions(contact.getName());

                    }
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SendDataChildActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("Alerte");
                            builder.setMessage("Etes vous sure de vouloir supprimer" + parent.getName());
                            builder.setPositiveButton("Oui",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Log_in.c.Pa
                                            addParent.setVisibility(View.VISIBLE);
                                            linearLayout.setVisibility(View.GONE);
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
                    });

                }
            }

            @Override
            public void OnError() {

            }
        });

    }


    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Log.i(TAG, "Name: " + name);
//                        Log.i(TAG, "Phone Number: " + phoneNo);
                        Contacts contactsObject = new Contacts(name, phoneNo);
//                        System.out.println(contactsObject.getName());
                        contactsArrayList.add(contactsObject);

                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
    }

    private void getCallDetails(String phoneNumber) {

        callLogList = new ArrayList<>();

        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            if (phoneNumber != null)
                if (phoneNumber.equals(managedCursor.getString(number))) {
                    String phNumber = managedCursor.getString(number);
                    String callType = managedCursor.getString(type);
                    String callDate = managedCursor.getString(date);
                    Date callDayTime = new Date(Long.valueOf(callDate));
                    String callDuration = managedCursor.getString(duration);
                    String dir = null;
                    int dircode = Integer.parseInt(callType);
                    switch (dircode) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            dir = getString(R.string.OutGoing);
                            break;

                        case CallLog.Calls.INCOMING_TYPE:
                            dir = getString(R.string.Incoming);
                            break;

                        case CallLog.Calls.MISSED_TYPE:
                            dir = getString(R.string.Missed);
                            break;
                    }
                    CallLogModel callLogModel = new CallLogModel(phNumber, callDayTime.toString() + " s", callDuration, dir);
                    callLogList.add(callLogModel);
                }
        }

        managedCursor.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkSMSPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            {
                Log.i(TAG, "Contacts permission NOT granted");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_PHONE_CALL);
                return;
            }
        }
        getAllSms(getApplicationContext());
    }


    private void checkContactPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            {
                Log.i(TAG, "Contacts permission NOT granted");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_CONTACTS,
                }, PERMS_CONTACT_ID);
                return;
            }
        }
        getContactList();
    }

    private void checkMapPermission() {
        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_MAP_ID);
            return;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CONTACT_ID) {
            checkContactPermissions();
        }
        if (requestCode == MY_READ_PERMISSION_CODE) {
        }

        if (requestCode == REQUEST_PHONE_CALL) {
            checkSMSPermissions();
        }
        if (requestCode == PERMS_MAP_ID) {
            checkMapPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkCallLogPermissions(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            {
                Log.i(TAG, "Contacts permission NOT granted");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, MY_READ_PERMISSION_CODE);
                return;
            }

        }
        getCallDetails(phoneNumber);
    }


}