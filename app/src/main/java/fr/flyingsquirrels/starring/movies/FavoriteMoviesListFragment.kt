package fr.flyingsquirrels.starring.movies

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.uber.autodispose.autoDisposable
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.movies.view.MovieAdapter
import fr.flyingsquirrels.starring.movies.viewmodel.FavoriteMoviesListViewModel
import fr.flyingsquirrels.starring.utils.MovieDiffCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteMoviesListFragment : BaseListFragment(){

    companion object {
        fun newInstance(args: Bundle? = null): FavoriteMoviesListFragment {
            val fragment = FavoriteMoviesListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }

    private val vm: FavoriteMoviesListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.isEnabled = false
        vm.getFavoriteMovies()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map { tvItems -> tvItems.sortedBy { it.title} }
                .autoDisposable(scopeProvider)
                .subscribe( {
                    if (list?.adapter == null) {
                        list?.adapter = MovieAdapter(it)
                    } else {
                        val diffCallback = MovieDiffCallback((list.adapter as MovieAdapter).items, it)
                        (list.adapter as MovieAdapter).items = it
                        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter as MovieAdapter)
                    }

                    finishLoading()
                }, this::handleError)
    }
}