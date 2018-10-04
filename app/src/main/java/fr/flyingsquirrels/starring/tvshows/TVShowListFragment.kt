package fr.flyingsquirrels.starring.tvshows

import android.os.Bundle
import android.view.View
import com.uber.autodispose.autoDisposable
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.model.TVShowResponse
import fr.flyingsquirrels.starring.tvshows.view.TVShowAdapter
import fr.flyingsquirrels.starring.tvshows.viewmodel.TVShowListViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject

class TVShowListFragment : BaseListFragment(){

    companion object {
        fun newInstance(args: Bundle? = null): TVShowListFragment {
            val fragment = TVShowListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }

    private val vm : TVShowListViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvListRequest: Single<TVShowResponse>? = when (arguments?.get(BaseListFragment.TYPE_KEY)) {
            TVShowResponse.POPULAR -> vm.getPopularTVShows()
            TVShowResponse.TOP_RATED -> vm.getTopRatedTVShows()
            TVShowResponse.AIRING_TODAY -> vm.getAiringTodayTVShows()
            TVShowResponse.ON_THE_AIR -> vm.getOnTheAirTVShows()
            else -> null
        }
        swipe_refresh.setOnRefreshListener {
            tvListRequest?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.autoDisposable(scopeProvider)?.subscribe(
                    { response ->
                        list?.adapter = response?.results?.let { TVShowAdapter(it) }
                        finishLoading()
                    }, this::handleError
            )
        }
        tvListRequest?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.autoDisposable(scopeProvider)?.subscribe(
                { response ->
                    list?.adapter = response?.results?.let { TVShowAdapter(it) }
                    finishLoading()
                }, this::handleError
        )
    }
}