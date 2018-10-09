package fr.flyingsquirrels.starring.tvshows

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.tvshows.view.TVShowAdapter
import fr.flyingsquirrels.starring.tvshows.viewmodel.FavoriteTVShowsListViewModel
import fr.flyingsquirrels.starring.utils.TVShowDiffCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTVShowsListFragment : BaseListFragment(){

    companion object {
        fun newInstance(args: Bundle? = null): FavoriteTVShowsListFragment {
            val fragment = FavoriteTVShowsListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }

    private val vm : FavoriteTVShowsListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.isEnabled = false
        vm.getFavoriteTVShows()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map { tvItems -> tvItems.sortedBy { it.name} }
                .subscribe( {
                    if (list?.adapter == null) {
                        list?.adapter = TVShowAdapter(it)
                    } else {
                        val diffCallback = TVShowDiffCallback((list.adapter as TVShowAdapter).items, it)
                        (list.adapter as TVShowAdapter).items = it
                        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter as TVShowAdapter)
                    }

                    finishLoading()
                }, this::handleError)?.let{
                    disposables.add(it)
                }
    }
}