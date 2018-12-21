package fr.flyingsquirrels.starring.people

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.model.PeopleResponse
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.people.view.PeopleAdapter
import fr.flyingsquirrels.starring.people.viewmodel.PeopleListViewModel
import fr.flyingsquirrels.starring.utils.PeopleDiffCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PeopleListFragment : BaseListFragment(){

    companion object {
        fun newInstance(args: Bundle? = null): PeopleListFragment {
            val fragment = PeopleListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }

    private val vm : PeopleListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paginator.onBackpressureDrop()
                .concatMap {
                    when (arguments?.get(BaseListFragment.TYPE_KEY)) {
                        PeopleResponse.POPULAR -> vm.getPopularPeople(it).toFlowable()
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
                        list?.adapter = PeopleAdapter(vm.list)
                    } else {
                        val newList = mutableListOf<Person>().apply {
                            addAll((list.adapter as PeopleAdapter).items)
                            addAll(results)
                        }
                        val diffCallback = PeopleDiffCallback(vm.list, newList)
                        vm.list.addAll(results)
                        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter as PeopleAdapter)
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