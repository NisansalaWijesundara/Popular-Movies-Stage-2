package com.example.android.popularmovies_stage2;

/**
 * Created by Nisansala on 12/24/2017.
 * The com.example.android.popularmovies_stage1.
 *
 * Image class to store movie poster's properties
 */

public class Image {
    private String title;
    private String releaseDate;
    private String voteAvg;
    private String synopsis;

    private String image;
    private String id;
    private String trailerLink;
    private String reviewLink;
    private String reviewAuthor;
    private String reviewContent;
    private byte[] moviePosterDb;
    public Image() {
    }

    public Image(String title, String releaseDate, String voteAvg, String synopsis, byte[] moviePosterDb, String trailerLink, String reviewLink, String reviewAuthor, String reviewContent) {
    }


    public String getTitle() {
        return title;
    }

    public String setTitle(String title) {
        this.title = title;
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {

        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
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

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
