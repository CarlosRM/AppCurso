package com.example.carlos.appcurso.Domain;

import android.graphics.drawable.Drawable;

/**
 * Created by Carlos on 28/01/2017.
 */

public class Song {
    private String title;
    private String album;
    private String artist;
    private String albumCover;
    private long songID;

    public Song () {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public long getSongID() {
        return songID;
    }

    public void setSongID(long songID) {
        this.songID = songID;
    }
}
