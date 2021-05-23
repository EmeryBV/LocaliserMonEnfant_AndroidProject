package com.example.localisermonenfant_enfant.activity.Media.Video;

import android.net.Uri;

public class VideoModel  {

    String videoTitle;
    String videoDuration;
    Uri videoUri;

    public VideoModel() {
    }

    public VideoModel(String videoTitle, String videoDuration, Uri videoUri) {
        this.videoTitle = videoTitle;
        this.videoDuration = videoDuration;
        this.videoUri = videoUri;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

}