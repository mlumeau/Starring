package com.flyingsquirrels.starring

import com.flyingsquirrels.starring.model.TMDBMovie
import com.flyingsquirrels.starring.model.TMDBMovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mlumeau on 26/02/2018.
 */


interface TMDBRetrofitService {
    companion object {
        const val MOVIE = "movie"
    }

    @GET("$MOVIE/${TMDBMovieResponse.TOP_RATED}")
    fun getTopRatedMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/${TMDBMovieResponse.POPULAR}")
    fun getPopularMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/${TMDBMovieResponse.NOW_PLAYING}")
    fun getNowPlayingMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/${TMDBMovieResponse.UPCOMING}")
    fun getUpcomingMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/{movieId}?append_to_response=credits%2Cvideos")
    fun getMovieDetails(@Path("movieId") movieId:Int): Call<TMDBMovie>
}