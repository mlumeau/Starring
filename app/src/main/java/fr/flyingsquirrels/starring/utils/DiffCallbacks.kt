package fr.flyingsquirrels.starring.utils

import androidx.recyclerview.widget.DiffUtil

import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.model.Searchable
import fr.flyingsquirrels.starring.model.TVShow

class MovieDiffCallback(private val mOldMovieList: List<Movie>, private val mNewMovieList: List<Movie>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldMovieList.size
    }

    override fun getNewListSize(): Int {
        return mNewMovieList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldMovieList[oldItemPosition].id === mNewMovieList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, _, _, _, _, _, _, _, _, _, _, id) = mOldMovieList[oldItemPosition]
        val (_, _, _, _, _, _, _, _, _, _, _, id1) = mNewMovieList[newItemPosition]

        return id == id1
    }
}

class TVShowDiffCallback(private val mOldTVShowList: List<TVShow>, private val mNewTVShowList: List<TVShow>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldTVShowList.size
    }

    override fun getNewListSize(): Int {
        return mNewTVShowList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldTVShowList[oldItemPosition].id === mNewTVShowList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, _, _, _, _, _,  id) = mOldTVShowList[oldItemPosition]
        val (_, _, _, _, _, _,  id1) = mNewTVShowList[newItemPosition]

        return id == id1
    }
}

class PeopleDiffCallback(private val mOldPeople: List<Person>, private val mNewPeople: List<Person>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldPeople.size
    }

    override fun getNewListSize(): Int {
        return mNewPeople.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldPeople[oldItemPosition].id === mNewPeople[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, _, _, _, _, _,  id) = mOldPeople[oldItemPosition]
        val (_, _, _, _, _, _,  id1) = mNewPeople[newItemPosition]

        return id == id1
    }
}

class SearchDiffCallback(private val mOldSearch: List<Searchable>, private val mNewSearch: List<Searchable>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldSearch.size
    }

    override fun getNewListSize(): Int {
        return mNewSearch.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldSearch[oldItemPosition]._id === mNewSearch[newItemPosition]._id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldSearch[oldItemPosition]._id === mNewSearch[newItemPosition]._id
    }
}