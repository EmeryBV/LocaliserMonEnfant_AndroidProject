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
import android.widget.TextView;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.R;

import java.util.List;

public class PictureActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PictureAdapter pictureAdapter;
    List<String> images;
    TextView picture_number;

    private static final int MY_READ_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        picture_number = findViewById(R.id.pictureNumber);
        recyclerView = findViewById(R.id.recycler_view);

        //Check from permission
        if (ContextCompat.checkSelfPermission(PictureActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(PictureActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        } else {
            loadImages();

        }

    }

    private void loadImages() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        images = ImagesGallery.listOfImage(this);
        pictureAdapter = new PictureAdapter(this, images, new PictureAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                Toast.makeText(PictureActivity.this, "" + path, Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(pictureAdapter);
        picture_number.setText("Photos ( " + images.size() + ")");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read external storage permission granted", Toast.LENGTH_SHORT).show();
                loadImages();
            } else {
                Toast.makeText(this, "Read external storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
