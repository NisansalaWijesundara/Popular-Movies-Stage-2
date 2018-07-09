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

    public Image() {

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
}
