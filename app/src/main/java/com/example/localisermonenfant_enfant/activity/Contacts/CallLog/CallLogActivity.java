package com.example.localisermonenfant_enfant.activity.Contacts.CallLog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.Date;

public class CallLogActivity extends AppCompatActivity {

    private String TAG = "TAG";

    ArrayList<CallLogModel>  callLogList = new ArrayList<>();

    String phoneNumber;

    private int contactID;
    private String contactName;
    private String contactPhoneNumber;
    private Connection.Contact contact;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        contactID = Integer.parseInt(intent.getStringExtra("contactID"));
        contactName = intent.getStringExtra("contactName");
        contactPhoneNumber = intent.getStringExtra("contactPhoneNumber");
        contact = new Connection.Contact(contactID,contactName,contactPhoneNumber);
        displayMessage();

    }


    public void displayMessage(){

        Log_in.c.GetCallData(getApplicationContext(), MainMenu.child, contact, new Connection.GetCallsCallback() {
            @Override
            public void Success(ArrayList<Connection.CallData> callDataList) {


                for (Connection.CallData connectionCallLog: callDataList) {


                    int dircode = Integer.parseInt(String.valueOf(connectionCallLog.getType()));
                    String dir ="";
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
                    CallLogModel callLog = new CallLogModel(String.valueOf(contactPhoneNumber)
                            ,connectionCallLog.getDate(),connectionCallLog.getDuration(),dir);
                    callLogList.add(callLog);
                }
                setContentView(R.layout.activity_call_log);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager
                                (getBaseContext()));

                TextView titleName = findViewById(R.id.titleName);
                titleName.setText(contactName);

                CallLogAdapter monAdapter = new CallLogAdapter(callLogList);
                recyclerView.setAdapter(monAdapter);

                CallLogAdapter callLogAdapter = new CallLogAdapter(callLogList);
                recyclerView.setAdapter(callLogAdapter);

            }

            @Override
            public void Error() {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement des appels" , Toast.LENGTH_LONG).show();
            }
        });

    }




}