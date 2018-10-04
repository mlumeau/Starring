package fr.flyingsquirrels.starring.people

import android.os.Bundle
import android.view.View
import com.uber.autodispose.autoDisposable
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.model.PeopleResponse
import fr.flyingsquirrels.starring.people.view.PeopleAdapter
import fr.flyingsquirrels.starring.people.viewmodel.PeopleListViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject

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

    private val vm : PeopleListViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val peopleListRequest: Single<PeopleResponse>? = when (arguments?.get(BaseListFragment.TYPE_KEY)) {
            PeopleResponse.POPULAR -> vm.getPopularPeople()
            else -> null
        }

        swipe_refresh.setOnRefreshListener {
            peopleListRequest?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.autoDisposable(scopeProvider)?.subscribe(
                    { response ->
                        list?.adapter = response?.people?.let { PeopleAdapter(it) }
                        finishLoading()
                    }, this::handleError
            )
        }
        peopleListRequest?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.autoDisposable(scopeProvider)?.subscribe(
                { response ->
                    list?.adapter = response?.people?.let { PeopleAdapter(it) }
                    finishLoading()
                }, this::handleError
        )
    }
}