package com.isuru.mymovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Isuru Senanayake on 16/03/2019.
 *
 * -- Movie object, attribute names needs to be Serialize the  according the the response of the API
 * -- Implemented by the Parcelable interface in order to Serialize and transfer as an object through activities
 */

public class Movie implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("backdrop_path")
    private String thumbImgUrl;

    @SerializedName("overview")
    private String description;

    @SerializedName("vote_average")
    private String rating;

    @SerializedName("release_date")
    private String relDate;

    @SerializedName("original_language")
    private String language;

    public Movie(){
        // Default constructor
    }

    protected Movie(Parcel in) {
        title = in.readString();
        thumbImgUrl = in.readString();
        description = in.readString();
        rating = in.readString();
        relDate = in.readString();
        language = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(thumbImgUrl);
        dest.writeString(description);
        dest.writeString(rating);
        dest.writeString(relDate);
        dest.writeString(language);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getThumbImgUrl() {
        return thumbImgUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    public String getRelDate() {
        return relDate;
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbImgUrl(String thumbImgUrl) {
        this.thumbImgUrl = thumbImgUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setRelDate(String relDate) {
        this.relDate = relDate;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
