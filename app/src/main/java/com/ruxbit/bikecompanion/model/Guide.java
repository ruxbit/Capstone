package com.ruxbit.bikecompanion.model;

import android.net.Uri;

public class Guide {
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch";
    private static final String YOUTUBE_IMAGE_URL = "https://img.youtube.com/vi";
    private static final String YOUTUBE_V = "v";
    private static final String YOUTUBE_IMAGE_DEFAULT = "hqdefault.jpg";

    private String name;
    private String videoId;

    public Guide(String name, String videoUrl) {
        this.name = name;
        this.videoId = Uri.parse(videoUrl).getQueryParameter(YOUTUBE_V);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Uri getVideoUri() {
        return Uri.parse(YOUTUBE_URL).buildUpon().appendQueryParameter(YOUTUBE_V, videoId).build();
    }

    public Uri getImageUri() {
        return Uri.parse(YOUTUBE_IMAGE_URL).buildUpon().appendPath(videoId).appendPath(YOUTUBE_IMAGE_DEFAULT).build();
    }
}
