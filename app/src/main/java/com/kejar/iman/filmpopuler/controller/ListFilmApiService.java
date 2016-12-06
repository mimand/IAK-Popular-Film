package com.kejar.iman.filmpopuler.controller;

import com.kejar.iman.filmpopuler.model.Film;
import com.kejar.iman.filmpopuler.model.ListFilm;
import com.kejar.iman.filmpopuler.model.Thriller;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by iman on 30/11/2016.
 */

public interface ListFilmApiService {
    @GET("movie/top_rated")
    Call<ListFilm> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<ListFilm> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<ListFilm> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Film> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<Thriller> getMovieThriller(@Path("id") int id, @Query("api_key") String apiKey);

}
