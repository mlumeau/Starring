package fr.flyingsquirrels.starring.movies.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.MovieResponse
import fr.flyingsquirrels.starring.movies.FavoriteMoviesListFragment
import fr.flyingsquirrels.starring.movies.MovieListFragment

class MoviesPagerAdapter(fm: FragmentManager, private val context: Context?): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            return if(position == 0) {
                FavoriteMoviesListFragment.newInstance()
            } else {
                val args = Bundle()
                args.putString(BaseListFragment.TYPE_KEY, when (position) {
                    1 -> MovieResponse.NOW_PLAYING
                    2 -> MovieResponse.UPCOMING
                    3 -> MovieResponse.POPULAR
                    4 -> MovieResponse.TOP_RATED
                    else -> null
                })

                MovieListFragment.newInstance(args)
            }
        }

        override fun getCount() = 5

        override fun getPageTitle(position: Int): String {
            return when(position){
                1 -> context?.getString(R.string.now_playing) ?: ""
                2 -> context?.getString(R.string.upcoming) ?: ""
                3 -> context?.getString(R.string.popular) ?: ""
                4 -> context?.getString(R.string.top_rated) ?: ""
                else -> ""
            }
        }
    }