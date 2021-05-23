package com.example.localisermonenfant_enfant.activity.Media.Video;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;

public class VideoListActivity extends AppCompatActivity {

    public static ArrayList<VideoModel > videoArrayList;
    RecyclerView recyclerView;
    public static final int PERMISSION_READ = 101;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (checkPermission()) {
            videoList();
        }
    }

    public void videoList() {

        getVideos();
    }

    //get video files from storage
    public void getVideos() {
        Log_in.c.GetVideos(getApplicationContext(), MainMenu.child, new Connection.GetVideosCallback() {
            @Override
            public void OnSuccess(ArrayList<Connection.Media> videoList) throws URISyntaxException {
                ArrayList<VideoModel> mediaList = new ArrayList<>();
                for (Connection.Media video: videoList) {
                    URI uri = new URI(video.getLink());
                    Uri newUri = new Uri.Builder().scheme(uri.getScheme())
                            .encodedAuthority(uri.getRawAuthority())
                            .encodedPath(uri.getRawPath())
                            .query(uri.getRawQuery())
                            .fragment(uri.getRawFragment())
                            .build();
                    VideoModel videoModel = new VideoModel(video.getName(),"60", newUri);
                    mediaList.add(videoModel);
                }

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                VideoAdapter  adapter = new VideoAdapter (getApplicationContext(), mediaList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int pos, View v) {
                        Intent intent = new Intent(getApplicationContext(), VideoPlayActivity.class);
                        intent.putExtra("pos", pos);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void OnError() {
                Log.e("Debug", "Erreur lors de la récupération des videos !  ");
            }
        });

    }

    public void setVideos() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                VideoModel  videoModel  = new VideoModel ();
                videoModel .setVideoTitle(title);
                videoModel .setVideoUri(Uri.parse(data));
                videoModel .setVideoDuration(timeConversion(Long.parseLong(duration)));
                videoArrayList.add(videoModel);

            } while (cursor.moveToNext());
        }


    }


    //time conversion
    public String timeConversion(long value) {
        String videoTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case  PERMISSION_READ: {
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                    } else {
                        videoList();
                    }
                }
            }
        }
    }
}