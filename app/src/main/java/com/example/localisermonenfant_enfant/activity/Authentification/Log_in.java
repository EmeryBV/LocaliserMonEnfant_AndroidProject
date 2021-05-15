package com.example.localisermonenfant_enfant.activity.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;

public class Log_in extends AppCompatActivity {

    EditText username;
    EditText password;
    TextView sign_in;
    TextView error;
    Button log_in;
    static Connection c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_log_in);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        sign_in = findViewById(R.id.sign_in);
        log_in = findViewById(R.id.btn_log_in);
        error = findViewById(R.id.Error);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Sign_in.class);
                startActivity(intent);
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    c = new Connection(username.getText().toString(), password.getText().toString(), Connection.ConnectionType.Parent, getApplication(), new Connection.ConnectionCallback() {
                    @Override
                    public void Success() {
                        Intent intent = new Intent(getApplication(), Sign_in.class);
                        startActivity(intent);
                    }
                    @Override
                    public void Error() {

                        error.setText(R.string.Error);
                    }
                });


            }
        });

    }
}