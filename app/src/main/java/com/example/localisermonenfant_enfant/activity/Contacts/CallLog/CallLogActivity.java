package com.example.localisermonenfant_enfant.activity.Contacts.CallLog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.MessageAdapter;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.Sms;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;
import com.example.localisermonenfant_enfant.activity.SendDataChild.SendDataChildActivity;

import java.util.ArrayList;
import java.util.Date;

public class CallLogActivity extends AppCompatActivity {

    private String TAG = "TAG";

    ArrayList<CallLogModel> callLogList = new ArrayList<>();

    String phoneNumber;

    private int contactID;
    private String contactName;
    private String contactPhoneNumber;
    private Connection.Contact contact;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);
        Intent intent = getIntent();
        contactID = Integer.parseInt(intent.getStringExtra("contactID"));
        contactName = intent.getStringExtra("contactName");
        contactPhoneNumber = intent.getStringExtra("contactPhoneNumber");
        contact = new Connection.Contact(contactID, contactName, contactPhoneNumber);
        displayMessage();


    }

    public void displayMessage() {

        Log_in.c.GetCallData(getApplicationContext(), MainMenu.child, contact, new Connection.GetCallsCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void Success(ArrayList<Connection.CallData> callDataList) {


                for (Connection.CallData connectionCallLog : callDataList) {


                    int dircode = connectionCallLog.getType();
                    Log.e("Dircode", String.valueOf(dircode));
                    String dir = "test";
                    switch (dircode) {
                        case 2:
                            dir = getString(R.string.OutGoing);
                            break;

                        case 1:
                            dir = getString(R.string.Incoming);
                            break;

                        case 3:
                            dir = getString(R.string.Missed);
                            break;
                    }

//                    Log.e("Deazeazeazezaebug", dir);
                    String dateString = new SimpleDateFormat("HH:mm-dd/MM/yyyy").format(new Date(connectionCallLog.getDate()));
                    CallLogModel callLog = new CallLogModel(String.valueOf(contactPhoneNumber)
                            , dateString, connectionCallLog.getDuration(), dir);
                    callLogList.add(callLog);
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager
                                (getBaseContext()));

                TextView titleName = findViewById(R.id.titleName);
                titleName.setText(contactName);

                CallLogAdapter monAdapter = new CallLogAdapter(callLogList);
                recyclerView.setAdapter(monAdapter);


            }

            @Override
            public void Error() {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement des appels", Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void getCallDetails(String phoneNumber, Context context) {
        SendDataChildActivity.callLogList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor managedCursor = cr.query(CallLog.Calls.CONTENT_URI, null,
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
                    Long callDayTime =Long.valueOf(callDate);
                    String callDuration = managedCursor.getString(duration);
                    String dir = null;
                    int dircode = Integer.parseInt(callType);
                    switch (dircode) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            dir = context.getString(R.string.OutGoing);
                            break;

                        case CallLog.Calls.INCOMING_TYPE:
                            dir = context.getString(R.string.Incoming);
                            break;

                        case CallLog.Calls.MISSED_TYPE:
                            dir = context.getString(R.string.Missed);
                            break;
                    }
                    CallLogModel callLogModel = new CallLogModel(phNumber, String.valueOf(callDayTime), callDuration, dir);
                    SendDataChildActivity.callLogList.add(callLogModel);
                }
        }

        managedCursor.close();
    }



}