package fr.flyingsquirrels.starring.people

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.people.view.PeopleAdapter
import fr.flyingsquirrels.starring.people.viewmodel.FavoritePeopleListViewModel
import fr.flyingsquirrels.starring.utils.PeopleDiffCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritePeopleListFragment : BaseListFragment(){

    companion object {
        fun newInstance(args: Bundle? = null): FavoritePeopleListFragment {
            val fragment = FavoritePeopleListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }

    private val vm: FavoritePeopleListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.isEnabled = false
        vm.getFavoritePeople()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map { tvItems -> tvItems.sortedBy { it.name} }
                .subscribe( {
                    if (list?.adapter == null) {
                        list?.adapter = PeopleAdapter(it)
                    } else {
                        val diffCallback = PeopleDiffCallback((list.adapter as PeopleAdapter).items, it)
                        (list.adapter as PeopleAdapter).items = it
                        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter as PeopleAdapter)
                    }

                    finishLoading()
                }, this::handleError)?.let{
                    disposables.add(it)
                }
    }


    override fun setPageNumber(pageNumber: Int) {
    }

    override fun getPageNumber(): Int {
        return 0
    }
}