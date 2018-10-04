package fr.flyingsquirrels.starring.tvshows.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.TVShowResponse
import fr.flyingsquirrels.starring.tvshows.FavoriteTVShowsListFragment
import fr.flyingsquirrels.starring.tvshows.TVShowListFragment

class TVShowsPagerAdapter(fm: FragmentManager, private val context: Context?): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            return if(position == 0) {
                FavoriteTVShowsListFragment.newInstance()
            } else {
                val args = Bundle()
                args.putString(BaseListFragment.TYPE_KEY, when (position) {
                    1 -> TVShowResponse.AIRING_TODAY
                    2 -> TVShowResponse.ON_THE_AIR
                    3 -> TVShowResponse.POPULAR
                    4 -> TVShowResponse.TOP_RATED
                    else -> null
                })

                TVShowListFragment.newInstance(args)
            }
        }

        override fun getCount() = 5

        override fun getPageTitle(position: Int): String {
            return when(position){
                1 -> context?.getString(R.string.airing_today) ?: ""
                2 -> context?.getString(R.string.on_the_air) ?: ""
                3 -> context?.getString(R.string.popular) ?: ""
                4 -> context?.getString(R.string.top_rated) ?: ""
                else -> ""
            }
        }
    }