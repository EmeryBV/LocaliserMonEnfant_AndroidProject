 package com.example.localisermonenfant_enfant.activity.MainMenu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.Child;
import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.Contacts.ContactsActivity;
import com.example.localisermonenfant_enfant.activity.Listen.ListenActivity;
import com.example.localisermonenfant_enfant.activity.Map.LocalisationChild;
import com.example.localisermonenfant_enfant.activity.Map.MapActivity;
import com.example.localisermonenfant_enfant.activity.Media.MediaActivity;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.SmsActivity;
import com.example.localisermonenfant_enfant.activity.Media.Picture.ImagesGallery;

import java.util.ArrayList;

 public class MainMenu extends AppCompatActivity {

    private int childId ;
    private  String childName = "";
    public static Connection.Child child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        childId = Integer.parseInt(intent.getStringExtra("childId"));
        childName = intent.getStringExtra("childName");
        Log.e("childId : ",  String.valueOf(childId));
        Log.e("childName : " , childName);
        child = new Connection.Child(childId,childName);
        setContentView(R.layout.activity_main_menu);
        Button media_button = findViewById(R.id.medias);

        Intent intent2 = new Intent(this, LocalisationChild.class);
        startService(intent2);

        media_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MediaActivity.class);
                startActivity(intent);
            }
        });
        Button contacts_button = findViewById(R.id.contacts);
        contacts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ContactsActivity.class);
                startActivity(intent);
            }
        });

        Button map_button = findViewById(R.id.map);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplication(), MapActivity.class);
                Intent intent = new Intent(getApplication(), MapActivity.class);
                startActivity(intent);
            }
        });

        Button listen_button = findViewById(R.id.listen);
        listen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ListenActivity.class);
                startActivity(intent);
            }
        });

        Button settings_button = findViewById(R.id.settings);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SettingsActivity.class);
                startActivity(intent);
            }
        });

    }
}
