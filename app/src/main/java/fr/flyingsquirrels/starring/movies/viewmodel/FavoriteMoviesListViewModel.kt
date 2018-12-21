package fr.flyingsquirrels.starring.movies.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.db.StarringDB

class FavoriteMoviesListViewModel(private val starringDB: StarringDB) : ViewModel() {
    fun getFavoriteMovies() = starringDB.favoritesDao().getFavoriteMovies()
}