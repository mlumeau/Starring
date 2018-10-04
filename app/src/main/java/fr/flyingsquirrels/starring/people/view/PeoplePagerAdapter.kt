package fr.flyingsquirrels.starring.people.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.PeopleResponse
import fr.flyingsquirrels.starring.people.FavoritePeopleListFragment
import fr.flyingsquirrels.starring.people.PeopleListFragment

class PeoplePagerAdapter(fm: FragmentManager, private val context: Context?): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            return if(position == 0) {
                FavoritePeopleListFragment.newInstance()
            } else {
                val args = Bundle()
                args.putString(BaseListFragment.TYPE_KEY, when (position) {
                    1 -> PeopleResponse.POPULAR
                    else -> null
                })

                PeopleListFragment.newInstance(args)
            }
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int): String {
            return when(position){
                1 -> context?.getString(R.string.popular) ?: ""
                else -> ""
            }
        }
    }