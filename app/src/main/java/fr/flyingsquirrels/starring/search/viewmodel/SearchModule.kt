package fr.flyingsquirrels.starring.search.viewmodel

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val SearchModule = module{

    viewModel{
        SearchViewModel(get())
    }
}