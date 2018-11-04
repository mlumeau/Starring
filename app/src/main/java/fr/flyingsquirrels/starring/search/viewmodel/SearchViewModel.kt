package fr.flyingsquirrels.starring.search.viewmodel

import fr.flyingsquirrels.starring.BaseListViewModel
import fr.flyingsquirrels.starring.model.Searchable
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import io.reactivex.processors.PublishProcessor

class SearchViewModel(private val tmdb: TMDBRetrofitService) : BaseListViewModel<Searchable>() {

    var query = PublishProcessor.create<String>()

    fun getSearchResults(query: String, page: Int) = tmdb.getSearchResults(query,page)
}