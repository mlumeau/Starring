package fr.flyingsquirrels.starring.tvshows

import android.os.Bundle
import android.view.View
import fr.flyingsquirrels.starring.BaseTabsFragment
import fr.flyingsquirrels.starring.tvshows.view.TVShowsPagerAdapter

class TVTabsFragment : BaseTabsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(TVShowsPagerAdapter(childFragmentManager, context))
    }
}
