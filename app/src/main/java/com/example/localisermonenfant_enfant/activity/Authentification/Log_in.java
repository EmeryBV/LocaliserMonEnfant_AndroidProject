package com.example.localisermonenfant_enfant.activity.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.ChoiceChild.ChoiceChildActivity;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;
import com.example.localisermonenfant_enfant.activity.SendDataChild.SendDataChildActivity;

public class Log_in extends AppCompatActivity {
    private String TAG = "TAG";
    TextView title;
    EditText username;
    EditText password;
    TextView sign_in;
    TextView error;
    Button log_in;
    boolean fromChild = false;
    public static Connection c;
    public static Connection linked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        username = findViewById(R.id.username);
        title = findViewById(R.id.Title);
        password = findViewById(R.id.password);
        sign_in = findViewById(R.id.sign_in);

        log_in = findViewById(R.id.btn_log_in);
        error = findViewById(R.id.Error);
        Intent intent = getIntent();
        fromChild = intent.getBooleanExtra("fromChild", false);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Sign_in.class);
                startActivity(intent);
            }
        });
        if(fromChild){
            sign_in.setVisibility(View.GONE);
            title.setText("Association");
            log_in.setText("Associer");
        }

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fromChild) {
                    c = new Connection(username.getText().toString(), password.getText().toString(), getApplicationContext(), new Connection.ConnectionCallback() {
                        @Override
                        public void Success() {
                            Intent intent = null;
                            if (Log_in.c.GetConnectionType().toString().equals("Parent"))
                                intent = new Intent(getApplication(), ChoiceChildActivity.class);
                            else
                                intent = new Intent(getApplication(), SendDataChildActivity.class);

                            if (intent != null) startActivity(intent);
                        }
                            @Override
                            public void Error () {
                                error.setText(R.string.Error);
                            }
                        });

                    }

                    if (fromChild) {

                        linked = new Connection(username.getText().toString(), password.getText().toString(), getApplicationContext(), new Connection.ConnectionCallback() {
                            @Override
                            public void Success() {
                                Intent intent = null;
                                Log.e("Children JSON Array", Log_in.linked.GetConnectionType().toString());
                                if (!Log_in.linked.GetConnectionType().toString().equals("Parent"))
                                    error.setText("Merci de vous connecter avec un compte parent");
                                else {
                                        intent = new Intent(getApplication(), SendDataChildActivity.class);
                                        Log_in.c.SetParent(getApplicationContext(), username.getText().toString(), password.getText().toString(), new Connection.SetParentCallback() {
                                            @Override
                                            public void OnSuccess() {
                                                Log.e("Debug", "Donnée envoyé");
                                            }

                                            @Override
                                            public void OnError() {
                                                Log.e("Debug", "Erreur");
                                            }
                                        });
                                }
                                if (intent != null) startActivity(intent);

                            }

                            @Override
                            public void Error() {
                                error.setText(R.string.Error);
                            }
                        });

                    }


                }
            });

        }
    }