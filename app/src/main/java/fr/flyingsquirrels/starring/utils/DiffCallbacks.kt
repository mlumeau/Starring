package fr.flyingsquirrels.starring.utils

import android.support.v7.util.DiffUtil

import fr.flyingsquirrels.starring.model.TMDBMovie
import fr.flyingsquirrels.starring.model.TMDBTVShow

class TMDBMovieDiffCallback(private val mOldTMDBMovieList: List<TMDBMovie>, private val mNewTMDBMovieList: List<TMDBMovie>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldTMDBMovieList.size
    }

    override fun getNewListSize(): Int {
        return mNewTMDBMovieList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldTMDBMovieList[oldItemPosition].id === mNewTMDBMovieList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, _, _, _, _, _, _, _, _, _, _, id) = mOldTMDBMovieList[oldItemPosition]
        val (_, _, _, _, _, _, _, _, _, _, _, id1) = mNewTMDBMovieList[newItemPosition]

        return id == id1
    }
}

class TMDBTVShowDiffCallback(private val mOldTMDBTVShowList: List<TMDBTVShow>, private val mNewTMDBTVShowList: List<TMDBTVShow>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldTMDBTVShowList.size
    }

    override fun getNewListSize(): Int {
        return mNewTMDBTVShowList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldTMDBTVShowList[oldItemPosition].id === mNewTMDBTVShowList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, _, _, _, _, _,  id) = mOldTMDBTVShowList[oldItemPosition]
        val (_, _, _, _, _, _,  id1) = mNewTMDBTVShowList[newItemPosition]

        return id == id1
    }
}