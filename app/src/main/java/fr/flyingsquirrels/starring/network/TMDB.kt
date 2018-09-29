package fr.flyingsquirrels.starring.network

import fr.flyingsquirrels.starring.model.*
import fr.flyingsquirrels.starring.network.TMDBCONST.AIRING_TODAY
import fr.flyingsquirrels.starring.network.TMDBCONST.MOVIE
import fr.flyingsquirrels.starring.network.TMDBCONST.NOW_PLAYING
import fr.flyingsquirrels.starring.network.TMDBCONST.ON_THE_AIR
import fr.flyingsquirrels.starring.network.TMDBCONST.PERSON
import fr.flyingsquirrels.starring.network.TMDBCONST.POPULAR
import fr.flyingsquirrels.starring.network.TMDBCONST.TOP_RATED
import fr.flyingsquirrels.starring.network.TMDBCONST.TV
import fr.flyingsquirrels.starring.network.TMDBCONST.UPCOMING
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mlumeau on 26/02/2018.
 */

object TMDBCONST {
    const val POSTER_URL_THUMBNAIL = "https://image.tmdb.org/t/p/w300"
    const val POSTER_URL_LARGE = "https://image.tmdb.org/t/p/w780"
    const val POSTER_URL_ORIGINAL = "https://image.tmdb.org/t/p/original"
    const val MOVIE = "movie"
    const val TV = "tv"
    const val PERSON = "person"

    const val TOP_RATED = "top_rated"
    const val POPULAR = "popular"
    const val NOW_PLAYING = "now_playing"
    const val UPCOMING = "upcoming"
    const val AIRING_TODAY = "airing_today"
    const val ON_THE_AIR = "on_the_air"
}

interface TMDBRetrofitService {

    @GET("$MOVIE/$TOP_RATED")
    fun getTopRatedMovies(): Observable<MovieResponse>

    @GET("$MOVIE/$POPULAR")
    fun getPopularMovies(): Observable<MovieResponse>

    @GET("$MOVIE/$NOW_PLAYING")
    fun getNowPlayingMovies(): Observable<MovieResponse>

    @GET("$MOVIE/$UPCOMING")
    fun getUpcomingMovies(): Observable<MovieResponse>

    @GET("$MOVIE/{movieId}?append_to_response=credits%2Cvideos%2Cimages")
    fun getMovieDetails(@Path("movieId") movieId: Int): Observable<Movie>

    @GET("$TV/$POPULAR")
    fun getPopularTVShows(): Observable<TVShowResponse>

    @GET("$TV/$TOP_RATED")
    fun getTopRatedTVShows(): Observable<TVShowResponse>

    @GET("$TV/$AIRING_TODAY")
    fun getAiringTodayTVShows(): Observable<TVShowResponse>

    @GET("$TV/$ON_THE_AIR")
    fun getOnTheAirTVShows(): Observable<TVShowResponse>

    @GET("$TV/{tvId}?append_to_response=credits%2Cvideos%2Cimages")
    fun getTVShowDetails(@Path("tvId") tvId: Int): Observable<TVShow>

    @GET("$TV/{tvId}/season/{seasonNumber}?append_to_response=videos")
    fun getTVShowSeasonDetails(@Path("tvId") tvId: Int,@Path("seasonNumber")  seasonNumber : Int): Observable<Season>

    @GET("$PERSON/$POPULAR")
    fun getPopularPeople(): Observable<PeopleResponse>

    @GET("$PERSON/{personId}?append_to_response=combined_credits%2Cimages")
    fun getPersonDetails(@Path("personId") personId: Int): Observable<Person>

}