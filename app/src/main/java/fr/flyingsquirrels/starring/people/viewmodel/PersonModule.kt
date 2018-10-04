package fr.flyingsquirrels.starring.people.viewmodel

import fr.flyingsquirrels.starring.persons.viewmodel.PersonDetailViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val PeopleModule = module{

    viewModel{
        PersonDetailViewModel(get(), get())
    }

    viewModel{
        PeopleListViewModel(get())
    }

    viewModel{
        FavoritePeopleListViewModel(get())
    }


}