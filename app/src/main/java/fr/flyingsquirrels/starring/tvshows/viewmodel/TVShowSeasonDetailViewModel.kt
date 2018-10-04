package fr.flyingsquirrels.starring.tvshows.viewmodel

import androidx.lifecycle.ViewModel
import fr.flyingsquirrels.starring.model.Season
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import io.reactivex.Single

class TVShowSeasonDetailViewModel(private val tmdb : TMDBRetrofitService) : ViewModel(){

    fun getTVShowSeasonDetails(idShow: Int, seasonNb: Int): Single<Season> = tmdb.getTVShowSeasonDetails(idShow, seasonNb)
}