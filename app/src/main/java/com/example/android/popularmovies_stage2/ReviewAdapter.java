package com.example.android.popularmovies_stage2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nisansala on 7/21/2018.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(@NonNull Context context, ArrayList<Review> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View reviewListItemView = convertView;
        if (reviewListItemView == null) {
            reviewListItemView = LayoutInflater.from(getContext()).inflate(R.layout.movie_review, parent, false);
        }
        final Review currentReview = getItem(position);
        TextView content = reviewListItemView.findViewById(R.id.review_content);
        content.setText("'" + currentReview.getReviewContent() + "'");
        TextView author = reviewListItemView.findViewById(R.id.review_author);
        author.setText(currentReview.getReviewAuthor());
        return reviewListItemView;
    }
}
