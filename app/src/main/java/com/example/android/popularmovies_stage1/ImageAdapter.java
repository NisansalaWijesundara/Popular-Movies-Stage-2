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

    private final ImageAdapterOnClickHandler mClickHandler;
    int resource;
    LayoutInflater vi;
    private ArrayList<String> imageURLs = new ArrayList<>(30);
    private Context activity_context;
    private List<Image> movies;
    private String url;
    private String LOG_TAG;
    private Handler handler;

   /* public ImageAdapter(Context applicationContext, int movie_posters, ArrayList<Image> movieList, MovieAdapterOnClickHandler handler) {
        vi = (LayoutInflater) applicationContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.handler = handler;
        this.activity_context = applicationContext;
        this.movieList = movieList;
        this.resource = movie_posters;
    }*/

    public ImageAdapter(Context applicationContext, List<Image> images, String movie_image__url, ImageAdapterOnClickHandler handler) {
        activity_context = applicationContext;
        movies = images;
        url = movie_image__url;

        mClickHandler = handler;
        Log.v("ImageAdapter", "ImageAdapter");
    }

    private Context getContext() {
        return activity_context;
    }
    ///public ImageAdapter(MainActivity mainActivity, ImageAdapterOnClickHandler mClickHandler) {
    //activity_context = applicationContext;
    //resource = movie_posters;
    //this.movieList = movieList;
    //this.handler = handler;
    //this.mClickHandler = mClickHandler;
    // this.mClickHandler = mClickHandler;

    //}


    void setImageURLs(List<Image> movieList) {

        this.movies = movieList;
        if (movieList == null) {
            Log.e(LOG_TAG, "movieList passes as null in Adapter.");
        } else {
            imageURLs.clear();
            for (int i = 0; i < movieList.size(); i++) {
                imageURLs.add(i, movieList.get(i).getImage());

            }
            notifyDataSetChanged();
            Log.v("ImageAdapter", "setImageURLs");
        }
    }

    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_posters;
        LayoutInflater inflater = LayoutInflater.from(context);
        //  boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        Log.v("ImageAdapter", "onCreateViewHolder");
        return new ImageAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ImageAdapterViewHolder holder, int position) {

        if (imageURLs == null) {
            holder.moviePoster.setImageResource(R.mipmap.ic_launcher);
            Log.v("ImageAdapter", "onBindViewHolder");
        } else {

            String currentImageURL = imageURLs.get(position);
            //holder.movieName.setText(movieList.get(position).getTitle());
            Picasso.with(activity_context).load(currentImageURL).into(holder.moviePoster);

            Log.v("ImageAdapter", "onBindViewHolder2");

        }
    }

    @Override
    public int getItemCount() {
        if (imageURLs == null) {
            return 0;
        }
        Log.v("ImageAdapter", "getItemCount");
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
            Log.v("ImageAdapter", "ImageAdapterViewHolder");
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Image movieData = movies.get(adapterPosition);
            mClickHandler.onClick(movieData);

            //mClickHandler.onClick(view, getAdapterPosition());
            Log.v("ImageAdapter","onClick");

        }
    }
}
