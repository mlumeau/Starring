package fr.flyingsquirrels.starring.tvshows.viewmodel

import fr.flyingsquirrels.starring.BaseListViewModel
import fr.flyingsquirrels.starring.model.TVShow
import fr.flyingsquirrels.starring.network.TMDBRetrofitService

class TVShowListViewModel(private val tmdb: TMDBRetrofitService) : BaseListViewModel<TVShow>() {
    fun getPopularTVShows(page: Int) = tmdb.getPopularTVShows(page)
    fun getTopRatedTVShows(page: Int) = tmdb.getTopRatedTVShows(page)
    fun getOnTheAirTVShows(page: Int) = tmdb.getOnTheAirTVShows(page)
    fun getAiringTodayTVShows(page: Int) = tmdb.getAiringTodayTVShows(page)
}