package fr.flyingsquirrels.starring.search

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.model.Searchable
import fr.flyingsquirrels.starring.search.view.SearchAdapter
import fr.flyingsquirrels.starring.search.viewmodel.SearchViewModel
import fr.flyingsquirrels.starring.utils.SearchDiffCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SearchListFragment : BaseListFragment(), SearchInterface{


    companion object {

        fun newInstance(args: Bundle? = null): SearchListFragment {
            val fragment = SearchListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }


    private val vm: SearchViewModel by sharedViewModel()

    private var currentPaginationSubscription: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.query.onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ query ->
                    currentPaginationSubscription?.let{
                        disposables.remove(it)
                    }

                    setPageNumber(0)
                    vm.list.clear()
                    list.adapter = null
                    loading.visibility = View.VISIBLE

                    paginator.onBackpressureDrop()
                            .concatMap{
                                vm.getSearchResults(query,it).toFlowable().subscribeOn(Schedulers.io())
                            }?.map {
                                it.results ?: listOf()
                            }
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({ results ->
                                if (list?.adapter == null ) {
                                    if(vm.pageNumber == 1){
                                        vm.list.clear()
                                    }
                                    vm.list.addAll(results)
                                    list?.adapter = SearchAdapter(vm.list)
                                } else {
                                    val newList = mutableListOf<Searchable>().apply {
                                        addAll((list.adapter as SearchAdapter).items)
                                        addAll(results)
                                    }
                                    val diffCallback = SearchDiffCallback(vm.list, newList)
                                    vm.list.addAll(results)
                                    DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter as SearchAdapter)
                                }

                                finishLoading()
                            }, this::handleError
                            )?.let{
                                currentPaginationSubscription = it
                                disposables.add(it)
                            }

                    nextPage()

                }, this::handleError)?.let {
                    disposables.add(it)
                }

        Handler().post {
            arguments?.getString(SearchInterface.QUERY)?.let{
                search(it)
            }
        }
    }

    override fun setPageNumber(pageNumber: Int) {
        vm.pageNumber = pageNumber
    }

    override fun getPageNumber(): Int {
        return vm.pageNumber
    }

    override fun search(query: String) {
        vm.query.onNext(query)
    }

}