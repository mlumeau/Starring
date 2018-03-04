package com.flyingsquirrels.starring

import com.flyingsquirrels.starring.model.TMDBMovie
import com.flyingsquirrels.starring.model.TMDBMovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by mlumeau on 26/02/2018.
 */


interface TMDBRetrofitService {
    companion object {
        const val MOVIE = "movie"
        const val MAX_AGE_SHORT = 7*24*3600
        const val MAX_AGE_LONG = 30*24*3600
    }

    @GET("$MOVIE/${TMDBMovieResponse.TOP_RATED}")
    @Headers("Cache-Control: max-age=$MAX_AGE_LONG")
    fun getTopRatedMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/${TMDBMovieResponse.POPULAR}")
    @Headers("Cache-Control: max-age=$MAX_AGE_LONG")
    fun getPopularMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/${TMDBMovieResponse.NOW_PLAYING}")
    @Headers("Cache-Control: max-age=$MAX_AGE_SHORT")
    fun getNowPlayingMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/${TMDBMovieResponse.UPCOMING}")
    @Headers("Cache-Control: max-age=$MAX_AGE_SHORT")
    fun getUpcomingMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/{movieId}?append_to_response=credits%2Cvideos")
    @Headers("Cache-Control: max-age=$MAX_AGE_LONG")
    fun getMovieDetails(@Path("movieId") movieId:Int): Call<TMDBMovie>
}