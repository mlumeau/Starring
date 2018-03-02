package com.flyingsquirrels.starring

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by mlumeau on 26/02/2018.
 */


interface TMDBRetrofitService {
    companion object {
        const val MOVIE = "movie"
    }

    @GET(MOVIE+"/"+TMDBMovieResponse.TOP_RATED)
    fun getTopRatedMovies(): Call<TMDBMovieResponse>
    @GET(MOVIE+"/"+TMDBMovieResponse.POPULAR)
    fun getPopularMovies(): Call<TMDBMovieResponse>
    @GET(MOVIE+"/"+TMDBMovieResponse.NOW_PLAYING)
    fun getNowPlayingMovies(): Call<TMDBMovieResponse>
    @GET(MOVIE+"/"+TMDBMovieResponse.UPCOMING)
    fun getUpcomingMovies(): Call<TMDBMovieResponse>
}