package com.example.android.popularmovies_stage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Class gets the movie posters from the list and
 * converts each movie poster into a RecyclerView.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder> {



    private final ImageAdapterOnClickHandler mClickHandler;
    private ArrayList<String> imageURLs = new ArrayList<>(30);
    private Context activity_context;
    private List<Image> movies;


    public ImageAdapter(Context applicationContext, ImageAdapterOnClickHandler handler) {

        activity_context = applicationContext;
        mClickHandler = handler;

    }


    void setImageURLs(List<Image> movieList) {

        this.movies = movieList;
        if (movieList == null) {

        } else {
            imageURLs.clear();
            for (int i = 0; i < movieList.size(); i++) {
                imageURLs.add(i, movieList.get(i).getImage());

            }
            notifyDataSetChanged();

        }
    }

    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.movie_posters;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);


        return new ImageAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ImageAdapterViewHolder holder, int position) {

        if (imageURLs == null) {
            holder.moviePoster.setImageResource(R.mipmap.ic_launcher);

        } else {

            String currentImageURL = imageURLs.get(position);

            Picasso.with(activity_context).load(currentImageURL).into(holder.moviePoster);


        }
    }

    @Override
    public int getItemCount() {

        if (imageURLs == null) {
            return 0;
        }

        return imageURLs.size();
    }

    public interface ImageAdapterOnClickHandler {

        void onClick(Image movieData);

    }

    public class ImageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView moviePoster;


        public ImageAdapterViewHolder(View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.movie_poster);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            Image movieData = movies.get(adapterPosition);
            mClickHandler.onClick(movieData);

        }
    }
}
