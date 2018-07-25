package com.example.marco.playermp3.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.marco.playermp3.Adapter.Track;
import com.example.marco.playermp3.Adapter.TrackAdapter;
import com.example.marco.playermp3.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

List<Track>tracklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        tracklist=new ArrayList<>();

        tracklist.add(new Track(321,"Alba",3.16,"Venditti","fddfgfjh"));
        tracklist.add(new Track(584,"Acqua",2.5,"Mina","jhkjhjkh"));

setupRecyclerView();


    }

    public void setupRecyclerView(){
        RecyclerView recyclerview=findViewById(R.id.recyclerView);
        TrackAdapter adapter=new TrackAdapter(tracklist, new TrackAdapter.OnItemListener() {
            @Override
            public void setItemClickListener(int position, Track track, View view) {
                Toast.makeText(MainActivity.this, "position:"+position, Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
    }


}
