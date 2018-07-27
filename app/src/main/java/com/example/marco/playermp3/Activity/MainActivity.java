package com.example.marco.playermp3.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marco.playermp3.Adapter.Track;
import com.example.marco.playermp3.Adapter.TrackAdapter;
import com.example.marco.playermp3.R;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_READ_STORAGE =10 ;
    List<Track> tracklist;
    private Uri musicuri;
    private Cursor musicursor;
    private ImageButton nextButton,prewievbutton,commandButton;
    private TextView barTracktest;
    private SeekBar progressbar;
    private Handler mhandler;
    private Runnable mrunnable;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying=false;
    private int currentPosition = 0;
    private boolean isplayingbutton = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nextButton=findViewById(R.id.nextButton);
        prewievbutton=findViewById(R.id.previewButton);
        commandButton=findViewById(R.id.commandButton);
        barTracktest=findViewById(R.id.testoTraccia);
        progressbar=findViewById(R.id.progressBar);


        prewievbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPosition>0){
                    if(!mediaPlayer.isPlaying()){
                        commandButton.setImageResource(R.drawable.ic_pause);
                        isplayingbutton=true;

                    }

                    changeSong(--currentPosition);
                }

            }
        });

        commandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isplayingbutton){
                    mediaPlayer.pause();
                    commandButton.setImageResource(R.drawable.ic_play);
                    isplayingbutton=false;
                }else{
                    mediaPlayer.start();
                    commandButton.setImageResource(R.drawable.ic_pause);
                    isplayingbutton=true;
                }

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPosition<tracklist.size()){
                    if(!mediaPlayer.isPlaying()){
                        commandButton.setImageResource(R.drawable.ic_pause);
                        isplayingbutton=true;

                    }

                    changeSong(++currentPosition);
                }


            }

        });


        //SI COLLEGA IL MEDIAPLAYER TRAMITE IL SEEKTO ALLA PROGRESSBAR
        progressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer != null && b){
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tracklist = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            !=PackageManager.PERMISSION_GRANTED){

            //IL PERMESSO NON E' CONCESSO
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    //MOSTRA ALERT VIEW
                    showAlert("Accetta Richiesta","Il permesso serve per leggere la musica sul tuo dispositivo");
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSION_REQUEST_READ_STORAGE);

                }

            }else{

            //PERMESSO GIA' CONCESSO
            getfileAudio();
            setupRecyclerView();
        }




        // LEGGI CANZONI DA ARCHIVIO
        getfileAudio();

        //SETUP RECYCLER
        setupRecyclerView();


    }

    public void setupRecyclerView() {
        RecyclerView recyclerview = findViewById(R.id.recyclerView);
        TrackAdapter adapter = new TrackAdapter(tracklist, new TrackAdapter.OnItemListener() {
            @Override
            public void setItemClickListener(int position, Track track, View view) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                currentPosition=position;
                barTracktest.setText(track.getTitle());
                commandButton.setImageResource(R.drawable.ic_pause);
                playsong(track.getPath());
                getValueMusic();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
    }

    private void getfileAudio() {
        ContentResolver contentResolver = getContentResolver();
        musicuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        musicursor = contentResolver.query(musicuri, null, null, null, null);

        if (musicursor != null && musicursor.moveToFirst()) {
            //get column

            int titleColumn = musicursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int authorsColumn = musicursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int pathColumn = musicursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                long thisid = musicursor.getLong(idColumn);
                String thistitle = musicursor.getString(titleColumn);
                String thisartist = musicursor.getString(authorsColumn);
                double thisduration = (musicursor.getInt(durationColumn) / 1000) / 60.0;
                thisduration = new BigDecimal(Double.toString(thisduration)).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                String path = musicursor.getString(pathColumn);

                tracklist.add(new Track(thisid, thistitle, thisduration, thisartist, path));

            } while (musicursor.moveToNext());

        }
    }
    private void showAlert(String title, String message){
        android.app.AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(MainActivity.this);
        }

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSION_REQUEST_READ_STORAGE);
                    }
                })
                .setNegativeButton("annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_STORAGE:

                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permesso concesso
                    Toast.makeText(getApplicationContext(), "Sto caricando le tue canzoni..."/*messagge*/, Toast.LENGTH_SHORT).show();

                    //leggi canzoni da archivio
                    getfileAudio();

                    //imposta il recicyclerView
                    setupRecyclerView();

                } else {
                    //dobbiamo far funzionare l'applicazione senza questo permesso
                }

                return;
        }

    }

    private void
    playsong(String path){


        if(isPlaying){
            mediaPlayer.stop();
            isPlaying=false;
        }

        try {

            mediaPlayer=new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

            isPlaying=true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void changeSong(int position) {
        mhandler.removeCallbacks(mrunnable);


        progressbar.setMax(mediaPlayer.getDuration() / 1000);
        playsong(tracklist.get(position).getPath());
        barTracktest.setText(tracklist.get(position).getTitle());
        progressbar.setProgress(0);
        getValueMusic();

    }


    //AGGIORNA LA SEEKBAR
    private void getValueMusic() {

        progressbar.setMax(mediaPlayer.getDuration() / 1000);
        mhandler = new Handler();

        mrunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    progressbar.setProgress(mCurrentPosition);
                    Log.d("CURRENT_TIME", String.valueOf(mCurrentPosition));
                }

                if (isTrackFinish()) {
                    changeSong(++currentPosition);
                }

                mhandler.postDelayed(this, 500);
            }
        };

        runOnUiThread(mrunnable);

    }






    //INDICA SE TRACCIA FINITA RETURN FALSE SE NON FINITA ALTRIMENTI TRUE
    private boolean isTrackFinish(){

        int finishTime=mediaPlayer.getDuration()/1000;
        int currentTime=mediaPlayer.getCurrentPosition()/1000;

        if (currentTime>=finishTime){
            return true;
        }

        return false;
    }




}




