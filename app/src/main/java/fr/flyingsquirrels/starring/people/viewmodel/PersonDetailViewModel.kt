package fr.flyingsquirrels.starring.persons.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.db.StarringDB
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import io.reactivex.Completable

class PersonDetailViewModel(private val tmdb: TMDBRetrofitService, private val starringDB: StarringDB) : ViewModel() {

    fun getPersonDetails(id: Int) = tmdb.getPersonDetails(id)
    fun getFavoritePersonWithId(id: Int?) = starringDB.favoritesDao().getFavoritePersonWithId(id)
    fun deleteFavoritePerson(person: Person): Completable = Completable.fromAction { starringDB.favoritesDao().deleteFavoritePerson(person) }
    fun insertFavoritePerson(person: Person): Completable = Completable.fromAction { starringDB.favoritesDao().insertFavoritePerson(person) }
}
