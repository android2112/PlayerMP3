package com.example.marco.playermp3.Adapter;

public class Track {

    private long id;

    public Track(long id, String title, Double duration, String artist, String path) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.artist = artist;
        this.path = path;
    }

    private String title;
    private Double duration;
    private String artist;
    private String path;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
