package com.example.android.popularmovies_stage2;

/**
 * Created by Nisansala on 7/25/2018.
 */

public class Review {
    String reviewLink = null;
    String reviewAuthor = null;
    String reviewContent = null;

    public Review(String reviewLink, String reviewAuthor, String reviewContent) {
        this.reviewLink = reviewLink;
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
    }

    public String getReviewLink() {
        return reviewLink;
    }

    public void setReviewLink(String reviewLink) {
        this.reviewLink = reviewLink;
    }


    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }
}
