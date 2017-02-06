package com.example.carlos.appcurso.UI;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlos.appcurso.Domain.MusicFilter;
import com.example.carlos.appcurso.Domain.MusicService;
import com.example.carlos.appcurso.Domain.Song;
import com.example.carlos.appcurso.R;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Carlos on 28/01/2017.
 */

public class MusicPlayer extends Fragment implements View.OnClickListener, Runnable {

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound=false;
    private boolean songStarted;

    SeekBar songSeekBar;
    TextView currentTime;
    TextView totalTime;
    int currentIndex = 0;
    ArrayList<Song> songList;
    View v;
    TextView songTitle;
    TextView artistName;
    TextView albumTitle;
    ImageView albumCover;
    ImageView playButton;
    ImageView nextButton;
    ImageView previousButton;
    boolean canUnbind;

    boolean isPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        super.onCreate(savedInstanceState);

        if(playIntent==null){
            playIntent = new Intent(getActivity(), MusicService.class);
            /*getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);*/
            getActivity().startService(playIntent);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);

        }

        if(v == null) {
            v = inflater.inflate(R.layout.fragment_music_player, container, false);
            initializeViews();
            run();
            setListeners();
            if(savedInstanceState!=null) {
                currentIndex = savedInstanceState.getInt("currentIndex");
                songStarted = savedInstanceState.getBoolean("songStarted");
                isPlaying = savedInstanceState.getBoolean("isPlaying");
                currentTime.setText(savedInstanceState.getString("currentTime"));
                updateUI();
            }
            updateUI();
        }

        setHasOptionsMenu(true);
        getActivity().setTitle("Music player");
        canUnbind = true;
        return v;
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setSongList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };

    public void setCurrentIndex (int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setIsPlaying (boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.call_button);
        item.setVisible(false);
        item = menu.findItem(R.id.browser_button);
        item.setVisible(false);
        item = menu.findItem(R.id.restart_memory);
        item.setVisible(false);
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_icon) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            getActivity().unbindService(musicConnection);
                            canUnbind = false;
                            getActivity().stopService(new Intent(getActivity(), MusicService.class));
                            NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancelAll();
                            SharedPreferences settings = getActivity().getSharedPreferences("Preferences", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("logged", false);
                            editor.apply();
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
                            getActivity().finish();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to log out?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        return true;
    }

    public void play(){
        musicService.setSong(currentIndex);
        musicService.playSong();
    }

    public void pause(){
        musicService.pauseSong();
    }

    public void resume(){
        musicService.resumeSong();
    }

    public int getDuration() {
        return musicService.getDuration();
    }

    public void setPosition(int position) {
        if(musicService!=null) musicService.setPosition(position);
    }

    public int getPosition() {
        return musicService.getPosition();
    }

    private void initializeViews() {
        //isPlaying = false;
        songStarted = false;
        //currentIndex = 0;
        songList = new ArrayList<>();
        ContentResolver cr = getActivity().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(uri, null, selection, null, sortOrder);
        int count = 0;

        if(cur != null) {
            count = cur.getCount();
            if (count > 0) {
                while (cur.moveToNext()) {
                    Song song = new Song();
                    String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    song.setTitle(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    song.setAlbum(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                    song.setArtist(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    song.setDuration(cur.getLong(cur.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    song.setSongID(cur.getLong(cur.getColumnIndex(MediaStore.Audio.Media._ID)));
                    song.setAlbumCover(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                    songList.add(song);
                }
            }

            cur.close();
        }

        songSeekBar = (SeekBar) v.findViewById(R.id.songSeekBar);
        currentTime = (TextView) v.findViewById(R.id.currentTime);
        totalTime = (TextView) v.findViewById(R.id.totalTime);
        currentTime.setText("0:00");
        totalTime.setText(msToMin(songList.get(currentIndex).getDuration()));
        songSeekBar.setMax((int)songList.get(currentIndex).getDuration());

        songTitle = (TextView) v.findViewById(R.id.song_title);
        artistName = (TextView) v.findViewById(R.id.artist_name);
        albumTitle = (TextView) v.findViewById(R.id.album_name);
        albumCover = (ImageView) v.findViewById(R.id.album_cover);
        previousButton = (ImageView) v.findViewById(R.id.previous_song);
        playButton = (ImageView) v.findViewById(R.id.play);
        nextButton = (ImageView) v.findViewById(R.id.next_song);
        songTitle.setSelected(true);
        artistName.setSelected(true);
        albumTitle.setSelected(true);
        updateUI();
    }

    private Handler handler = new Handler();

    @Override
    public void run() {
        if(musicService!=null && musicBound && musicService.getInitialized() && musicService.getPrepared() && musicService.isPlaying()) {
            songSeekBar.setProgress(getPosition());
            currentTime.setText(msToMin(getPosition()));
        }
        handler.postDelayed(this,1);
    }

    private String msToMin (long ms) {
        String result = String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(ms),
                TimeUnit.MILLISECONDS.toSeconds(ms) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms))
        );
        return result;
    }

    private String secondsToMin (int seconds) {
        String result = String.format("%d:%02d",
                TimeUnit.SECONDS.toMinutes((long) seconds),
                TimeUnit.SECONDS.toSeconds((long) seconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long) seconds))
        );
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        outstate.putBoolean("songStarted",songStarted);
        outstate.putBoolean("isPlaying",isPlaying);
        outstate.putInt("currentIndex",currentIndex);
        outstate.putString("currentTime",currentTime.getText().toString());
    }

    private void setListeners() {
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) {
                    currentTime.setText(msToMin(i));
                    setPosition(i);
                    if(i == songList.get(currentIndex).getDuration()) {
                        if(isPlaying) {
                            playButton.setImageResource(R.drawable.ic_play_button);
                            isPlaying = false;
                        }
                    }
                } else if(i >= songList.get(currentIndex).getDuration()-500) {
                    if(currentIndex < songList.size()-1 && isPlaying){
                        setPosition(0);
                        ++currentIndex;
                        updateUI();
                        play();
                        songStarted = true;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateUI(){
        Log.d("ISPLAYING",isPlaying+"");
        songTitle.setText(songList.get(currentIndex).getTitle());
        artistName.setText(songList.get(currentIndex).getArtist());
        albumTitle.setText(songList.get(currentIndex).getAlbum());
        currentTime.setText("0:00");
        totalTime.setText(msToMin(songList.get(currentIndex).getDuration()));
        songSeekBar.setProgress(0);
        songSeekBar.setMax((int)songList.get(currentIndex).getDuration());
        if(isPlaying){
            playButton.setImageResource(R.drawable.ic_pause_button);
        } else {
            playButton.setImageResource(R.drawable.ic_play_button);
        }


        Cursor cursor = getActivity().managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{String.valueOf(songList.get(currentIndex).getAlbumCover())},
                null);

        Drawable drawable = null;
        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            drawable = Drawable.createFromPath(path);
        }
        albumCover.setImageDrawable(drawable);
    }

    @Override
    public void onDestroy(){
        //getActivity().stopService(playIntent);
        musicService = null;
        if(canUnbind) getActivity().unbindService(musicConnection);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if(!isPlaying){
                    playButton.setImageResource(R.drawable.ic_pause_button);
                    isPlaying=!isPlaying;
                    if(songSeekBar.getProgress() == songList.get(currentIndex).getDuration()) {
                        setPosition(0);
                        ++currentIndex;
                        updateUI();
                        play();
                        songStarted = true;
                    } else {
                        if(!songStarted){
                            play();
                            songStarted = true;
                        } else {
                            resume();
                        }
                    }
                }else{
                    isPlaying = !isPlaying;
                    playButton.setImageResource(R.drawable.ic_play_button);
                    songStarted = true;
                    pause();
                }
                break;
            case R.id.next_song:
                if(currentIndex < songList.size()-1){
                    setPosition(0);
                    ++currentIndex;
                    updateUI();
                    if(isPlaying) {
                        play();
                        songStarted = true;
                    } else {
                        songStarted = false;
                    }
                }
                break;
            case R.id.previous_song:
                //if(songStarted) {
                    if(getPosition()>1500 && isPlaying) {
                        setPosition(0);
                    }
                    else if(getPosition()>1500 && !isPlaying) setPosition(0);
                    else if (currentIndex > 0) {
                        setPosition(0);
                        --currentIndex;
                        updateUI();
                        if (isPlaying) {
                            play();
                            songStarted = true;
                        } else {
                            songStarted = false;
                        }
                    }
                break;
        }
    }
}
