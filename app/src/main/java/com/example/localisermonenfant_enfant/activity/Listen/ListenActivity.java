package com.example.localisermonenfant_enfant.activity.Listen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.localisermonenfant_enfant.R;

import java.io.IOException;

public class ListenActivity extends AppCompatActivity {
    private String TAG = "TAG";
    private static final int PERMS_LISTEN_ID = 1259;
    TextView instruction;
    TextView stateRecording;
    Button start_end_button;
    boolean recording = false;
    MediaRecorder mediaRecorder;
    private MediaRecorder recorder = null;
    private static String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audioRecord.3gp";
        instruction = findViewById(R.id.instruction);
        stateRecording = findViewById(R.id.stateRecording);
        start_end_button = findViewById(R.id.startRecordingButton);
        checkContactPermissions();

        start_end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!recording){
                    startRecording();
                    instruction.setText(R.string.EndSaveVoice);
                    start_end_button.setText("end");
                    stateRecording.setVisibility(View.VISIBLE);
                    recording =true;
                }
              else{
                    stopRecording();
                    instruction.setText(R.string.StartSaveVoice);
                    start_end_button.setText("start");
                    stateRecording.setVisibility(View.INVISIBLE);
                    recording =false;
                }
            }
        });

    }

    private void checkContactPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            {
                Log.i(TAG, "Contacts permission NOT granted");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.RECORD_AUDIO,
                }, PERMS_LISTEN_ID);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_LISTEN_ID) {
            checkContactPermissions();
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }



}
