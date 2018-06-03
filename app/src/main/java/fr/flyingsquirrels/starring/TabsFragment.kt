package fr.flyingsquirrels.starring

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.flyingsquirrels.starring.model.TMDBMovieResponse
import fr.flyingsquirrels.starring.model.TMDBTVShowResponse
import kotlinx.android.synthetic.main.fragment_tabs.*

class MovieTabsFragment : TabsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(MoviesPagerAdapter(childFragmentManager))
    }
}


class TVTabsFragment : TabsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(TVShowsPagerAdapter(childFragmentManager))
    }
}

abstract class TabsFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }

    protected fun init(adapter: FragmentStatePagerAdapter) {
        pager.adapter = adapter
        pager.currentItem = 1

        val tabs = (activity as MainActivity).tabs

        tabs.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(pager){

            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0 && context!=null) {
                    val ta = context!!.obtainStyledAttributes(kotlin.intArrayOf(R.attr.tabSelectedTextColor))
                    val selectedColor = ta.getResourceId(0, android.R.color.black)
                    ta.recycle()
                    tab.icon?.setColorFilter(ContextCompat.getColor(context!!, selectedColor), PorterDuff.Mode.SRC_IN)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    tab.icon?.setColorFilter(ContextCompat.getColor(context!!, R.color.grey600), PorterDuff.Mode.SRC_IN)
                }
            }
        })

        tabs.setupWithViewPager(pager)
        tabs.getTabAt(0)?.icon = ContextCompat.getDrawable(context!!,R.drawable.ic_star_black_24dp)
        tabs.getTabAt(0)?.icon?.setColorFilter(ContextCompat.getColor(context!!, R.color.grey600), PorterDuff.Mode.SRC_IN)

    }

    inner class MoviesPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment {

            val args = Bundle()
            val type = when(position){
                0 -> MediaListFragment.FAV_MOVIE
                1 -> TMDBMovieResponse.NOW_PLAYING
                2 -> TMDBMovieResponse.UPCOMING
                3 -> TMDBMovieResponse.POPULAR
                4 -> TMDBMovieResponse.TOP_RATED
                else -> null
            }

            args.putString(MediaListFragment.TYPE_KEY,type)

            return MediaListFragment.newInstance(args)
        }

        override fun getCount() = 5

        override fun getPageTitle(position: Int): String {
            return when(position){
                1 -> this@TabsFragment.getString(R.string.now_playing)
                2 -> this@TabsFragment.getString(R.string.upcoming)
                3 -> this@TabsFragment.getString(R.string.popular)
                4 -> this@TabsFragment.getString(R.string.top_rated)
                else -> ""
            }
        }
    }

    inner class TVShowsPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment {

            val args = Bundle()
            val type = when(position){
                0 -> MediaListFragment.FAV_TV
                1 -> TMDBTVShowResponse.AIRING_TODAY
                2 -> TMDBTVShowResponse.ON_THE_AIR
                3 -> TMDBTVShowResponse.POPULAR
                4 -> TMDBTVShowResponse.TOP_RATED
                else -> null
            }

            args.putString(MediaListFragment.TYPE_KEY,type)

            return MediaListFragment.newInstance(args)
        }

        override fun getCount() = 5

        override fun getPageTitle(position: Int): String {
            return when(position){
                1 -> this@TabsFragment.getString(R.string.airing_today)
                2 -> this@TabsFragment.getString(R.string.on_the_air)
                3 -> this@TabsFragment.getString(R.string.popular)
                4 -> this@TabsFragment.getString(R.string.top_rated)
                else -> ""
            }
        }
    }
}