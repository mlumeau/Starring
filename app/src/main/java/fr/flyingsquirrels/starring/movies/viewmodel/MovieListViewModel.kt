package fr.flyingsquirrels.starring.movies.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.network.TMDBRetrofitService

class MovieListViewModel(private val tmdb: TMDBRetrofitService) : ViewModel() {
    fun getPopularMovies() = tmdb.getPopularMovies()
    fun getTopRatedMovies() = tmdb.getTopRatedMovies()
    fun getUpcomingMovies() = tmdb.getUpcomingMovies()
    fun getNowPlayingMovies() = tmdb.getNowPlayingMovies()
}