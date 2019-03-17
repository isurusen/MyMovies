package com.isuru.mymovies.services;

import com.isuru.mymovies.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Isuru Senanayake on 15/03/2019.
 *
 * -- This is the API interface to use in Retrofit
 */

public interface ServicesInterface {

    @GET("movie/popular")
    Call<MovieResponse> getMovieListPopular(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<MovieResponse> getMovieListNowPlaying(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getMovieListTopRated(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MovieResponse> getMovieListUpComing(@Query("api_key") String apiKey);
}
