package com.example.localisermonenfant_enfant.activity.Media.Picture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;

import java.util.ArrayList;
import java.util.List;

public class PictureActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PictureAdapter pictureAdapter;
    TextView picture_number;

    private static final int MY_READ_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        picture_number = findViewById(R.id.pictureNumber);
        recyclerView = findViewById(R.id.recycler_view);

        Log_in.c.GetImages(getApplicationContext(), MainMenu.child, new Connection.GetImagesCallback() {
            @Override
            public void OnSuccess(ArrayList<Connection.Media> imageList) {
                ArrayList<String> imageURLs = new ArrayList<>();
                for (Connection.Media image: imageList) {
                    imageURLs.add(image.getLink());
                }

                loadImages(imageURLs);
                Log.e("Debug", "Image Récupéré !  ");

            }

            @Override
            public void OnError() {
                Log.e("Debug", "Erreur lors de la récupération des images !  ");
            }
        });

    }

    private void loadImages(ArrayList images) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        pictureAdapter = new PictureAdapter(this, images, new PictureAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                Toast.makeText(PictureActivity.this, "" + path, Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(pictureAdapter);
        picture_number.setText("Photos ( " + images.size() + ")");
    }


}
