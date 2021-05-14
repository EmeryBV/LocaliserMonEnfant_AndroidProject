package com.example.localisermonenfant_enfant.activity.Media;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.activity.Media.Documents.DocumentsActivity;
import com.example.localisermonenfant_enfant.activity.Media.Picture.PictureActivity;

import com.example.localisermonenfant_enfant.activity.Media.Video.VideoListActivity;
import com.example.localisermonenfant_enfant.activity.Media.WebHistory.WebHistoryActivity;

public class MediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);


        TextView picture_button = findViewById(R.id.btnPicture);
        picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), PictureActivity.class);
                startActivity(intent);
            }
        });

        TextView video_button = findViewById(R.id.btnVideo);
        video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), VideoListActivity.class);
                startActivity(intent);
            }
        });


        TextView webHistory_button = findViewById(R.id.btnWebHistory);
        webHistory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), WebHistoryActivity.class);
                startActivity(intent);
            }
        });

    }

}
