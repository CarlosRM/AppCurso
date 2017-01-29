package com.example.carlos.appcurso.Domain;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Carlos on 28/01/2017.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener {

    int currentPosition = 0;
    MediaPlayer player;
    ArrayList<Song> songList;
    int songIndex;
    private final IBinder musicBind = new MusicBinder();
    boolean prepared = false;
    boolean initialized = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    public void onCreate() {
        super.onCreate();
        songIndex = 0;
        player = new MediaPlayer();
        initializeMusicPlayer();
        initialized = true;

    }

    public void initializeMusicPlayer() {
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setSongList(ArrayList<Song> songlist) {
        this.songList = songlist;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public boolean getPrepared() {
        return prepared;
    }

    public boolean getInitialized(){
        return initialized;
    }

    public void playSong() {
        player.reset();
        Song currentSong = songList.get(songIndex);
        long currentSongID = currentSong.getSongID();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSongID);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
        prepared = true;
    }

    public void pauseSong() {
        if(player != null) player.pause();
    }

    public void resumeSong() {
        if(player!=null) player.start();
    }
    public int getPosition() {
        if(player!=null && prepared) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    public void setPosition(int position) {
        if(player!=null && initialized && prepared) {
            player.seekTo(position);
        }
        currentPosition = position;
    }
    ;
    public int getDuration() {
        if(player!=null && initialized && prepared) return player.getDuration();
        return 0;
    }

    public void setSong(int songIndex){
        this.songIndex=songIndex;
    }

    public boolean isPlaying() {
        if(player!=null && initialized && prepared && player.isPlaying()) return true;
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        //player.stop();
        //player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.seekTo(currentPosition);
        player.start();
    }

    @Override
    public void onDestroy(){
        Log.d("ONDESTROY","ondestroy");
        player.stop();
        super.onDestroy();
    }
}
