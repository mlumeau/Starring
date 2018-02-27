package com.flyingsquirrels.starring

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by mlumeau on 26/02/2018.
 */


interface TMDBRetrofitService {
    @GET("movie/top_rated")
    fun getTopRated(): Call<TMDBMovieResponse>
}