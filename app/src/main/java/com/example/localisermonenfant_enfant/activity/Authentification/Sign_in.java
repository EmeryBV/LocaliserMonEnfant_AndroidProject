package com.example.localisermonenfant_enfant.activity.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;

public class Sign_in extends AppCompatActivity {

    EditText pseudo;
    EditText password;
    Button sign_in;
    TextView log_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        pseudo = findViewById(R.id.pseudo);
        pseudo = findViewById(R.id.password);
        sign_in = findViewById(R.id.btn_sign_in);
        log_in = findViewById(R.id.log_in);

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Log_in.class);
                startActivity(intent);
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainMenu.class);
                startActivity(intent);
            }
        });

    }
}