package com.flyingsquirrels.starring

import com.google.gson.annotations.SerializedName

/**
 * Created by mlumeau on 02/03/2018.
 */
data class TMDBMovieResponse(@field:SerializedName("results")  var results: List<TMDBMovie>){
    companion object {
        const val TOP_RATED= "top_rated"
        const val POPULAR= "popular"
        const val NOW_PLAYING= "now_playing"
        const val UPCOMING= "upcoming"
    }
}
data class TMDBMovie(@field:SerializedName("poster_path") private var _posterPath: String,
                     @field:SerializedName("adult") var isAdult: Boolean,
                     @field:SerializedName("overview") var overview: String,
                     @field:SerializedName("release_date") var releaseDate: String,
                     @field:SerializedName("genre_ids") var genreIds: List<Int>,
                     @field:SerializedName("id") var id: Int,
                     @field:SerializedName("original_title") var originalTitle: String,
                     @field:SerializedName("original_language") var originalLanguage: String,
                     @field:SerializedName("title") var title: String,
                     @field:SerializedName("backdrop_path") private var _backdropPath: String,
                     @field:SerializedName("popularity") var popularity: Double,
                     @field:SerializedName("vote_count") var voteCount: Int?,
                     @field:SerializedName("video") var video: Boolean?,
                     @field:SerializedName("vote_average") var voteAverage: Double) {

    companion object {
        const val POSTER_URL = "https://image.tmdb.org/t/p/w300"
    }

    var posterPath = _posterPath
        get(): String {
            return POSTER_URL + _posterPath
        }
    var backdropPath = _backdropPath
        get(): String {
            return POSTER_URL + _backdropPath
        }
}