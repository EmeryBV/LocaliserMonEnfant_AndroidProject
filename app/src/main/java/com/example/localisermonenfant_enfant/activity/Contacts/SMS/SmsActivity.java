package com.example.localisermonenfant_enfant.activity.Contacts.SMS;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.Contacts.Contacts;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SmsActivity extends AppCompatActivity {
    private String TAG = "TAG";

    public static ArrayList<Sms> listSms = new ArrayList<>();
    static HashMap<String , String > HM_phone_name = new HashMap<>();
    private String phoneNumberContact="";

    private int contactID;
    private String contactName;
    private String phoneNumber;
    public static Connection.Contact contact;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        contactID = Integer.parseInt(intent.getStringExtra("contactID"));
        contactName = intent.getStringExtra("contactName");
        phoneNumber = intent.getStringExtra("contactPhoneNumber");
        contact = new Connection.Contact(contactID,contactName,phoneNumber);

        displayMessage();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
    }

    public void displayMessage(){
        Intent intent = getIntent();
        phoneNumberContact=  intent.getStringExtra("contactPhoneNumber");
        listSms = new ArrayList<>();
        Log_in.c.GetSMS(getApplicationContext(), MainMenu.child, contact, new Connection.GetSMSCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void Success(ArrayList<Connection.SMS> smsList) {
//                Toast.makeText(getApplicationContext(), "JE SUIS LA " , Toast.LENGTH_LONG).show();
                for (Connection.SMS connectionSmsList: smsList) {
                    if(phoneNumberContact.equals(connectionSmsList.getContact().getNum())) {
                        String dateString = new SimpleDateFormat("HH:mm-dd/MM/yyyy").format(new Date(connectionSmsList.getDate()));
                        Sms sms = new Sms(String.valueOf(connectionSmsList.getID()), connectionSmsList.getText(),
                                dateString, connectionSmsList.getContact().getName(), connectionSmsList.isSended() ? "sent" : "receive");

                        listSms.add(sms);
                    }
                }

                setContentView(R.layout.activity_sms);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_gchat);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager
                                (getBaseContext()));

                TextView titleName = findViewById(R.id.titleName);
                titleName.setText(contactName);

                MessageAdapter monAdapter = new MessageAdapter(listSms);
                recyclerView.setAdapter(monAdapter);
                recyclerView.scrollToPosition(monAdapter.getItemCount()-1);
            }

            @Override
            public void Error() {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement des SMS" , Toast.LENGTH_LONG).show();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void getAllSms(Context context) {
//        Toast.makeText(getApplicationContext(), "mon message" +  phoneNumber, Toast.LENGTH_SHORT).show();
        ContentResolver cr = context.getContentResolver();
        listSms = new ArrayList<>();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);

        int totalSMS = 0;
        if (c != null) {
            totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    if(contact.getNum().equals(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)))) {
                        Sms sms = new Sms();
                        sms.setId(String.valueOf(j));
                        sms.setCreatedAt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE)));

                        sms.setMessage(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY)));
                        if (!HM_phone_name.containsKey(contact.getNum()))
                            sms.setSender(getContactbyPhoneNumber(context, contact.getNum()));
                        else sms.setSender(HM_phone_name.get(contact.getNum()));

                        String type = "";
                        switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                            case Telephony.Sms.MESSAGE_TYPE_INBOX:
                                type = "receive";
                                sms.setType(type);
                                break;
                            case Telephony.Sms.MESSAGE_TYPE_SENT:
                                type = "sent";
                                sms.setType(type);
                                break;

                            default:
                                break;
                        }
                        listSms.add(sms);
                    }
                    c.moveToNext();
                }
            }
            c.close();

        } else {
            Toast.makeText(context, "No message to show!", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getContactbyPhoneNumber(Context c, String phoneNumber) {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = c.getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            return phoneNumber;
        } else {
            String name = phoneNumber;
            try {

                if (cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                }

            } finally {
                cursor.close();
            }

            HM_phone_name.put(phoneNumber,name);
            return name;
        }
    }



}