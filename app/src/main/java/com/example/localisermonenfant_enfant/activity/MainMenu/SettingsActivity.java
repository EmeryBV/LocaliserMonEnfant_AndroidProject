package com.example.localisermonenfant_enfant.activity.MainMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.activity.ChoiceChild.ChoiceChildActivity;
import com.example.localisermonenfant_enfant.activity.Map.areaMap;
import com.example.localisermonenfant_enfant.activity.SendDataChild.SendDataChildActivity;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    public static String notificationType ="Pop up";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup = findViewById(R.id.radioGrp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                notificationType = radioButton.getText().toString();
                if(notificationType.equals("Vibration")){
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }
                }else if(notificationType.equals("Pop up")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Alerte");
                    builder.setMessage("Vous avez activé la notification pop up");
                    builder.setPositiveButton("Oui",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                            Log_in.c.Pa
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(SettingsActivity.this, "Channel ID")
                            .setSmallIcon(R.drawable.ic_baseline_warning_24)
                            .setContentTitle("Alerte")
                            .setContentText("Vous avez activé les notifications")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(SettingsActivity.this);
                    notificationManager.notify(10, builder.build());
                }
            }
        });

    }
}
