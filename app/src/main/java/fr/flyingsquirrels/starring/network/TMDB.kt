package fr.flyingsquirrels.starring.network

import fr.flyingsquirrels.starring.model.*
import fr.flyingsquirrels.starring.network.TMDB_CONST.AIRING_TODAY
import fr.flyingsquirrels.starring.network.TMDB_CONST.MOVIE
import fr.flyingsquirrels.starring.network.TMDB_CONST.NOW_PLAYING
import fr.flyingsquirrels.starring.network.TMDB_CONST.ON_THE_AIR
import fr.flyingsquirrels.starring.network.TMDB_CONST.POPULAR
import fr.flyingsquirrels.starring.network.TMDB_CONST.TOP_RATED
import fr.flyingsquirrels.starring.network.TMDB_CONST.TV
import fr.flyingsquirrels.starring.network.TMDB_CONST.UPCOMING
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mlumeau on 26/02/2018.
 */

object TMDB_CONST {
    const val POSTER_URL_THUMBNAIL = "https://image.tmdb.org/t/p/w300"
    const val POSTER_URL_LARGE = "https://image.tmdb.org/t/p/w780"
    const val POSTER_URL_ORIGINAL = "https://image.tmdb.org/t/p/original"
    const val MOVIE = "movie"
    const val TV = "tv"

    const val TOP_RATED = "top_rated"
    const val POPULAR = "popular"
    const val NOW_PLAYING = "now_playing"
    const val UPCOMING = "upcoming"
    const val AIRING_TODAY = "airing_today"
    const val ON_THE_AIR = "on_the_air"
}

interface TMDBRetrofitService {

    @GET("$MOVIE/$TOP_RATED")
    fun getTopRatedMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/$POPULAR")
    fun getPopularMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/$NOW_PLAYING")
    fun getNowPlayingMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/$UPCOMING")
    fun getUpcomingMovies(): Call<TMDBMovieResponse>

    @GET("$MOVIE/{movieId}?append_to_response=credits%2Cvideos%2Cimages")
    fun getMovieDetails(@Path("movieId") movieId: Int): Call<TMDBMovie>

    @GET("$TV/$POPULAR")
    fun getPopularTVShows(): Call<TMDBTVShowResponse>

    @GET("$TV/$TOP_RATED")
    fun getTopRatedTVShows(): Call<TMDBTVShowResponse>

    @GET("$TV/$AIRING_TODAY")
    fun getAiringTodayTVShows(): Call<TMDBTVShowResponse>

    @GET("$TV/$ON_THE_AIR")
    fun getOnTheAirTVShows(): Call<TMDBTVShowResponse>

    @GET("$TV/{tvId}?append_to_response=credits%2Cvideos%2Cimages")
    fun getTVShowDetails(@Path("tvId") tvId: Int): Call<TMDBTVShow>

    @GET("$TV/{tvId}/season/{seasonNumber}?append_to_response=videos")
    fun getTVShowSeasonDetails(@Path("tvId") tvId: Int,@Path("seasonNumber")  seasonNumber : Int): Call<Season>

}