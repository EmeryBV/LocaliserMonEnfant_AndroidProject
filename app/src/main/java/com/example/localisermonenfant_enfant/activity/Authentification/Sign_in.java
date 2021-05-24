package com.example.localisermonenfant_enfant.activity.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.ChoiceChild.ChoiceChildActivity;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;
import com.example.localisermonenfant_enfant.activity.SendDataChild.SendDataChildActivity;

public class Sign_in extends AppCompatActivity {

    EditText pseudo;
    EditText password;
    Button sign_in;
    TextView log_in;
    TextView error;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        pseudo = findViewById(R.id.pseudo);
        password = findViewById(R.id.password);
        sign_in = findViewById(R.id.btn_sign_in);
        log_in = findViewById(R.id.log_in);
        radioGroup = findViewById(R.id.radioGrp);
        error = findViewById(R.id.Error);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = findViewById(selectedId);

                if(pseudo.getText().toString() .equals("")|| pseudo.getText().toString().equals(""))
                    error.setText(R.string.Error_signIn);
                else{


                    if(radioButton.getText().toString().equals(getString(R.string.Parent))){
                        Connection.SignUp(getApplicationContext(), pseudo.getText().toString(), pseudo.getText().toString(), password.getText().toString(), false, new Connection.SignUpCallback() {
                            @Override
                            public void OnSuccess() {
                                Log.e("Debug", "Envoie du parent reussi");
                            }

                            @Override
                            public void OnError() {
                                Log.e("Debug", "Erreur lors de l'inscription du parent  ");
                            }
                        });

                    }else{
                        Connection.SignUp(getApplicationContext(), pseudo.getText().toString(), pseudo.getText().toString(), password.getText().toString(), true, new Connection.SignUpCallback() {
                                    @Override
                                    public void OnSuccess() {
                                        Log.e("Debug", "Envoie de l'enfant reussi ");
                                    }

                                    @Override
                                    public void OnError() {
                                        Log.e("Debug", "Erreur lors de l'inscription de l'enfant  ");
                                    }
                                });

                    }
                    intent = new Intent(getApplication(), Log_in.class);
                    startActivity(intent);
                }



            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Log_in.class);
                startActivity(intent);
            }
        });

    }
}