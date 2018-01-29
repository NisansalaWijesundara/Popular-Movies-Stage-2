package com.example.android.popularmovies_stage1;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder> {

   /* private ArrayList<Image> movie_image;
    private Context context;

    public ImageAdapter(Context context,ArrayList<Image> movie_images){
        this.context = context;
        this.movie_image = movie_images;

    }
    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_posters, parent, false);
        return new ImageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapterViewHolder holder, int position) {

        Picasso.with(context).load(String.valueOf(movie_image.get(position))).resize(40, 40).into(holder.img_movie);
    }

    @Override
    public int getItemCount() {
        return movie_image.size();
    }

    public class ImageAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView img_movie;
        TextView title;
        public ImageAdapterViewHolder(View itemView) {

            super(itemView);

            img_movie = (ImageView)itemView.findViewById(R.id.movie_poster);

        }
    }*/

    //private final MovieAdapterOnClickHandler handler;
    int resource;
    LayoutInflater vi;
    private ArrayList<String> imageURLs = new ArrayList<>(15);
    private Context activity_context;
    private ArrayList<Image> movieList;
    private String LOG_TAG;



   /* public ImageAdapter(Context applicationContext, int movie_posters, ArrayList<Image> movieList, MovieAdapterOnClickHandler handler) {
        vi = (LayoutInflater) applicationContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.handler = handler;
        this.activity_context = applicationContext;
        this.movieList = movieList;
        this.resource = movie_posters;
    }*/

    public ImageAdapter(Context context, MovieAdapterOnClickHandler handler) {
        //this.handler = handler;
        activity_context = context;
    }

    public ImageAdapter(Context applicationContext, int movie_posters, List<Image> movieList, Handler handler) {
    }

    void setImageURLs(List<Image> movieList) {

        this.movieList = (ArrayList<Image>) movieList;
        if (movieList == null) {
            Log.e(LOG_TAG, "movieList passes as null in Adapter.");
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
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ImageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapterViewHolder holder, int position) {

        if (imageURLs == null) {
            holder.moviePoster.setImageResource(R.mipmap.ic_launcher);

        } else {
            String currentImageURL = imageURLs.get(position);
            Picasso.with(activity_context).load(currentImageURL).resize(40, 40).into(holder.moviePoster);

        }
    }

    @Override
    public int getItemCount() {
        if (imageURLs == null) {
            return 1;
        }

        return imageURLs.size();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Image thisMovie);
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
            Image dataForThisMovie = movieList.get(adapterPosition);
            //handler.onClick(dataForThisMovie);
        }
    }
}
