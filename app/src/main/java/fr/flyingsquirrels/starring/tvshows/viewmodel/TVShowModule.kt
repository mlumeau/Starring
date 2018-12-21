package fr.flyingsquirrels.starring.tvshows.viewmodel

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val TVShowModule = module{

    viewModel{
        TVShowDetailViewModel(get(),get())
    }

    viewModel{
        TVShowSeasonDetailViewModel(get())
    }

    viewModel{
        TVShowListViewModel(get())
    }

    viewModel{
        FavoriteTVShowsListViewModel(get())
    }


}