package fr.flyingsquirrels.starring.people

import android.os.Bundle
import android.view.View
import fr.flyingsquirrels.starring.BaseTabsFragment
import fr.flyingsquirrels.starring.people.view.PeoplePagerAdapter

class PeopleTabsFragment : BaseTabsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(PeoplePagerAdapter(childFragmentManager,context))
    }
}