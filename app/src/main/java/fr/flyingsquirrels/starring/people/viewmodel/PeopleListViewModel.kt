package fr.flyingsquirrels.starring.people.viewmodel

import fr.flyingsquirrels.starring.BaseListViewModel
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.network.TMDBRetrofitService

class PeopleListViewModel(private val tmdb: TMDBRetrofitService) : BaseListViewModel<Person>() {
    fun getPopularPeople(page: Int) = tmdb.getPopularPeople(page)
}