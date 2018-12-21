package fr.flyingsquirrels.starring.search.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.model.Searchable
import fr.flyingsquirrels.starring.model.Searchable.Companion.MOVIE
import fr.flyingsquirrels.starring.model.Searchable.Companion.PERSON
import fr.flyingsquirrels.starring.model.Searchable.Companion.TV
import fr.flyingsquirrels.starring.model.TVShow
import fr.flyingsquirrels.starring.movies.view.MovieAdapter
import fr.flyingsquirrels.starring.people.view.PeopleAdapter
import fr.flyingsquirrels.starring.tvshows.view.TVShowAdapter
import fr.flyingsquirrels.starring.utils.inflate


class SearchAdapter(var items: List<Searchable>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =  when(items[position].mediaType){
        MOVIE -> (holder as? MovieAdapter.MovieHolder)?.bind(items[position] as Movie)
        TV -> (holder as? TVShowAdapter.TVShowHolder)?.bind(items[position] as TVShow)
        PERSON -> (holder as? PeopleAdapter.PeopleHolder)?.bind(items[position] as Person)
        else -> null
    }!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType){
        MOVIE_TYPE -> MovieAdapter.MovieHolder(parent.inflate(R.layout.adapter_movies)!!)
        TV_TYPE ->  TVShowAdapter.TVShowHolder(parent.inflate(R.layout.adapter_movies)!!)
        PERSON_TYPE -> PeopleAdapter.PeopleHolder(parent.inflate(R.layout.adapter_people)!!)
        else -> null
    }!!

    override fun getItemCount(): Int = items.size


    override fun getItemViewType(position: Int): Int {
        return when(items[position].mediaType){
            MOVIE -> MOVIE_TYPE
            TV -> TV_TYPE
            PERSON -> PERSON_TYPE
            else -> -1
        }
    }

    companion object {
        const val MOVIE_TYPE = 0
        const val TV_TYPE = 1
        const val PERSON_TYPE = 2
    }

}