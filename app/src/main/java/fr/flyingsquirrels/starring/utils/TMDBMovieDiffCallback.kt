package fr.flyingsquirrels.starring.utils

import android.support.v7.util.DiffUtil

import fr.flyingsquirrels.starring.model.TMDBMovie

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

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}