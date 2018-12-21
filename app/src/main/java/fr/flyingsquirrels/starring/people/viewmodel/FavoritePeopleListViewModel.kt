package fr.flyingsquirrels.starring.people.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.db.StarringDB

class FavoritePeopleListViewModel(private val starringDB: StarringDB) : ViewModel() {
    fun getFavoritePeople() = starringDB.favoritesDao().getFavoritePeople()
}