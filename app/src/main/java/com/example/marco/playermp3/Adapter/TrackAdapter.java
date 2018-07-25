package com.example.marco.playermp3.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marco.playermp3.R;

import org.w3c.dom.Text;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    public interface onItemListener{
        void setItemClickListener(int position, Track track, View view);
    }

    private List<Track>tracklist;
    private onItemListener onItemListaner;

    public TrackAdapter(List<Track> tracklist, onItemListener onItemListaner) {
        this.tracklist = tracklist;
        this.onItemListaner = onItemListaner;
    }

    @NonNull
    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return null;
        }

    @Override
    public void onBindViewHolder(@NonNull TrackAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return tracklist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView duration;
        private TextView artist;
        private RelativeLayout touchrow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            duration=itemView.findViewById(R.id.duration);
            artist=itemView.findViewById(R.id.artist);
            touchrow=itemView.findViewById(R.id.touchrow);
        }
    }
}
