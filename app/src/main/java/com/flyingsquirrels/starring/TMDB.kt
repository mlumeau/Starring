package com.flyingsquirrels.starring

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by mlumeau on 26/02/2018.
 */


interface TMDBRetrofitService {
    @GET("movie/top_rated")
    fun getTopRatedMovies(): Call<TMDBMovieResponse>
    @GET("movie/popular")
    fun getPopularMovies(): Call<TMDBMovieResponse>
    @GET("movie/now_playing")
    fun getNowPlayingMovies(): Call<TMDBMovieResponse>
    @GET("movie/upcoming")
    fun getUpcomingMovies(): Call<TMDBMovieResponse>
}