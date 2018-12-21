package fr.flyingsquirrels.starring.movies

import android.os.Bundle
import android.view.View
import fr.flyingsquirrels.starring.BaseTabsFragment
import fr.flyingsquirrels.starring.movies.view.MoviesPagerAdapter

class MovieTabsFragment : BaseTabsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(MoviesPagerAdapter(childFragmentManager, context))
    }
}