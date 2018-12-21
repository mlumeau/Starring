package fr.flyingsquirrels.starring.movies.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.db.StarringDB
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import io.reactivex.Completable

class MovieDetailViewModel(private val tmdb: TMDBRetrofitService, private val starringDB: StarringDB) : ViewModel() {

    fun getMovieDetails(id: Int) = tmdb.getMovieDetails(id)
    fun getFavoriteMovieWithId(id: Int?) = starringDB.favoritesDao().getFavoriteMovieWithId(id)
    fun deleteFavoriteMovie(movie: Movie): Completable = Completable.fromAction { starringDB.favoritesDao().deleteFavoriteMovie(movie) }
    fun insertFavoriteMovie(movie: Movie): Completable = Completable.fromAction { starringDB.favoritesDao().insertFavoriteMovie(movie) }
}
