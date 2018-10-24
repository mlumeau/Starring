package fr.flyingsquirrels.starring.tvshows

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.model.TVShow
import fr.flyingsquirrels.starring.model.TVShowResponse
import fr.flyingsquirrels.starring.tvshows.view.TVShowAdapter
import fr.flyingsquirrels.starring.tvshows.viewmodel.TVShowListViewModel
import fr.flyingsquirrels.starring.utils.TVShowDiffCallback
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

        paginator.onBackpressureDrop()
                .concatMap {
                    when (arguments?.get(BaseListFragment.TYPE_KEY)) {
                        TVShowResponse.POPULAR -> vm.getPopularTVShows(it).toFlowable()
                        TVShowResponse.TOP_RATED -> vm.getTopRatedTVShows(it).toFlowable()
                        TVShowResponse.AIRING_TODAY -> vm.getAiringTodayTVShows(it).toFlowable()
                        TVShowResponse.ON_THE_AIR -> vm.getOnTheAirTVShows(it).toFlowable()
                        else -> null
                    }?.subscribeOn(Schedulers.io())
                }?.map {
                    it.results ?: listOf()
                }?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ results ->
                    if (list?.adapter == null) {
                        if(vm.pageNumber == 1){
                            vm.list.clear()
                        }
                        vm.list.addAll(results)
                        list?.adapter = TVShowAdapter(vm.list)
                    } else {
                        val newList = mutableListOf<TVShow>().apply {
                            addAll((list.adapter as TVShowAdapter).items)
                            addAll(results)
                        }
                        val diffCallback = TVShowDiffCallback(vm.list, newList)
                        vm.list.addAll(results)
                        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter as TVShowAdapter)
                    }
                    finishLoading()
                }, this::handleError
                )?.let{
                    disposables.add(it)
                }

        nextPage()
    }


    override fun setPageNumber(pageNumber: Int) {
        vm.pageNumber = pageNumber
    }

    override fun getPageNumber(): Int {
        return vm.pageNumber
    }
}