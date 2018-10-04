package fr.flyingsquirrels.starring.movies.viewmodel

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val MovieModule = module{

     viewModel{
        MovieDetailViewModel(get(), get())
    }

    viewModel{
        MovieListViewModel(get())
    }

    viewModel{
        FavoriteMoviesListViewModel(get())
    }

}