package fr.flyingsquirrels.starring.tvshows.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.db.StarringDB

class FavoriteTVShowsListViewModel(private val starringDB: StarringDB) : ViewModel() {
    fun getFavoriteTVShows() = starringDB.favoritesDao().getFavoriteTVShows()
}