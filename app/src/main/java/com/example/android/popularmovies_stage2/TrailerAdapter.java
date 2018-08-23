package com.example.android.popularmovies_stage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nisansala on 7/21/2018.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private Context context;
    private ArrayList<Trailer> mTrailers;

    TrailerAdapter(Context context, ArrayList<Trailer> resource) {
        super(context, 0, resource);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View trailerListView = convertView;
        if (trailerListView == null) {
            trailerListView = LayoutInflater.from(getContext()).inflate(R.layout.movie_trailers, parent, false);
        }
        final Trailer currentTrailer = getItem(position);
        TextView title = trailerListView.findViewById(R.id.video_name);
        title.setText("Trailer " + String.valueOf(position + 2));
        ImageButton playButton = trailerListView.findViewById(R.id.trailer_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(currentTrailer.getTrailerLink()));
                getContext().startActivity(webIntent);
            }
        });
        return trailerListView;

    }
}
