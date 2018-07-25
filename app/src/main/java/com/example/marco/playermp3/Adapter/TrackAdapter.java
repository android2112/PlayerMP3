package com.example.marco.playermp3.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marco.playermp3.R;

import org.w3c.dom.Text;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    public interface OnItemListener {
        void setItemClickListener(int position, Track track, View view);
    }

    private List<Track> trackList;
    private OnItemListener onItemListener;
    //private ViewHolder holder;

    public TrackAdapter(List<Track> trackList, OnItemListener onItemListener) {
        this.trackList = trackList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        //this.holder = holder;

        final Track currentTrack = trackList.get(position);

        holder.title.setText(currentTrack.getTitle());
        holder.artist.setText(currentTrack.getArtist());
        holder.duration.setText(String.valueOf(currentTrack.getDuration()));

        holder.touch_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemListener != null) {
                    onItemListener.setItemClickListener(position, currentTrack, holder.touch_row);
                }
            }
        });
    }
/*
    public void isPlaying() {
        holder.isPlaying.setVisibility(View.VISIBLE);
        holder.isPlaying.setImageResource(R.drawable.mp3logo);
    }
*/
    @Override
    public int getItemCount() {
        return trackList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView duration;
        private TextView artist;
        private RelativeLayout touch_row;
        private ImageView isPlaying;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            artist = itemView.findViewById(R.id.artist);
            touch_row = itemView.findViewById(R.id.touchrow);
            //isPlaying = itemView.findViewById(R.id.isPlay);

        }
    }
}
