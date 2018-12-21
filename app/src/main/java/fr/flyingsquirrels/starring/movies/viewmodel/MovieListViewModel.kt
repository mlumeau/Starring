package fr.flyingsquirrels.starring.movies.viewmodel

import fr.flyingsquirrels.starring.BaseListViewModel
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.network.TMDBRetrofitService

class MovieListViewModel(private val tmdb: TMDBRetrofitService) : BaseListViewModel<Movie>() {

    fun getPopularMovies(page: Int) = tmdb.getPopularMovies(page)
    fun getTopRatedMovies(page: Int) = tmdb.getTopRatedMovies(page)
    fun getUpcomingMovies(page: Int) = tmdb.getUpcomingMovies(page)
    fun getNowPlayingMovies(page: Int) = tmdb.getNowPlayingMovies(page)
}