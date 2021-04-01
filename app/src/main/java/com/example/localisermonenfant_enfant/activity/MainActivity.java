package com.example.localisermonenfant_enfant.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.activity.Contacts.ContactsActivity;
import com.example.localisermonenfant_enfant.activity.Listen.ListenActivity;
import com.example.localisermonenfant_enfant.activity.Media.MediaActivity;
import com.example.localisermonenfant_enfant.activity.SMS.SmsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button media_button = findViewById(R.id.medias);

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
                Intent intent = new Intent(getApplication(), SmsActivity.class);
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
