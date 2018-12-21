package fr.flyingsquirrels.starring.tvshows.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.db.StarringDB
import fr.flyingsquirrels.starring.model.TVShow
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import io.reactivex.Completable


class TVShowDetailViewModel(private val tmdb: TMDBRetrofitService, private val starringDB: StarringDB) : ViewModel() {

    fun getTVShowDetails(id: Int) = tmdb.getTVShowDetails(id)
    fun getFavoriteTVShowWithId(id: Int?) = starringDB.favoritesDao().getFavoriteTVShowWithId(id)
    fun deleteFavoriteTVShow(TVShow: TVShow): Completable = Completable.fromAction { starringDB.favoritesDao().deleteFavoriteTVShow(TVShow) }
    fun insertFavoriteTVShow(TVShow: TVShow): Completable = Completable.fromAction { starringDB.favoritesDao().insertFavoriteTVShow(TVShow) }
}
