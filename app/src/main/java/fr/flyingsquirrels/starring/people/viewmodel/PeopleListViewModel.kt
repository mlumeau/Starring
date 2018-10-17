package fr.flyingsquirrels.starring.people.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.network.TMDBRetrofitService

class PeopleListViewModel(private val tmdb: TMDBRetrofitService) : ViewModel() {
    fun getPopularPeople(page: Int) = tmdb.getPopularPeople(page)
}