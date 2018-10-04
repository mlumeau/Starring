package fr.flyingsquirrels.starring.tvshows.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.network.TMDBRetrofitService

class TVShowListViewModel(private val tmdb: TMDBRetrofitService) : ViewModel() {
    fun getPopularTVShows() = tmdb.getPopularTVShows()
    fun getTopRatedTVShows() = tmdb.getTopRatedTVShows()
    fun getOnTheAirTVShows() = tmdb.getOnTheAirTVShows()
    fun getAiringTodayTVShows() = tmdb.getAiringTodayTVShows()
}