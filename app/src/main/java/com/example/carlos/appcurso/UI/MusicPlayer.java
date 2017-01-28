package com.example.carlos.appcurso.UI;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlos.appcurso.Domain.MusicFilter;
import com.example.carlos.appcurso.Domain.Song;
import com.example.carlos.appcurso.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Carlos on 28/01/2017.
 */

public class MusicPlayer extends Fragment implements View.OnClickListener {

    int currentIndex;
    ArrayList<Song> songList;
    View v;
    TextView songTitle;
    TextView artistName;
    TextView albumTitle;
    ImageView albumCover;
    ImageView playButton;
    ImageView nextButton;
    ImageView previousButton;

    boolean isPlaying;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_music_player, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Music player");
        initializeViews();
        setListeners();
        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.call_button);
        item.setVisible(false);
        item = menu.findItem(R.id.browser_button);
        item.setVisible(false);
    }

    private void initializeViews() {
        isPlaying = false;
        currentIndex = 0;
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
                    Log.d("NAME,", cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    song.setTitle(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    song.setAlbum(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                    song.setArtist(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    song.setSongID(cur.getLong(cur.getColumnIndex(MediaStore.Audio.Media._ID)));
                    song.setAlbumCover(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                    songList.add(song);
                }
            }

            cur.close();
        }

        songTitle = (TextView) v.findViewById(R.id.song_title);
        artistName = (TextView) v.findViewById(R.id.artist);
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

    private void setListeners() {
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
    }

    private void updateUI(){
        songTitle.setText(songList.get(currentIndex).getTitle());
        artistName.setText(songList.get(currentIndex).getArtist());
        albumTitle.setText(songList.get(currentIndex).getAlbum());


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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if(!isPlaying){
                    playButton.setImageResource(R.drawable.ic_pause_button);
                }else{
                    playButton.setImageResource(R.drawable.ic_play_button);
                }
                isPlaying = !isPlaying;
                break;
            case R.id.next_song:
                if(currentIndex < songList.size()-1){
                    ++currentIndex;
                    updateUI();
                }
                break;
            case R.id.previous_song:
                if(currentIndex > 0) {
                    --currentIndex;
                    updateUI();
                }
        }
    }
}
