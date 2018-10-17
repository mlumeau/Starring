package fr.flyingsquirrels.starring.movies

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.model.MovieResponse
import fr.flyingsquirrels.starring.movies.view.MovieAdapter
import fr.flyingsquirrels.starring.movies.viewmodel.MovieListViewModel
import fr.flyingsquirrels.starring.utils.MovieDiffCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieListFragment : BaseListFragment(){
    companion object {
        fun newInstance(args: Bundle? = null): MovieListFragment {
            val fragment = MovieListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }

    private val vm: MovieListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paginator.onBackpressureDrop()
                .concatMap{
                    when (arguments?.get(BaseListFragment.TYPE_KEY)) {
                        MovieResponse.POPULAR -> vm.getPopularMovies(it).toFlowable()
                        MovieResponse.TOP_RATED -> vm.getTopRatedMovies(it).toFlowable()
                        MovieResponse.NOW_PLAYING -> vm.getNowPlayingMovies(it).toFlowable()
                        MovieResponse.UPCOMING -> vm.getUpcomingMovies(it).toFlowable()
                        else -> null
                    }?.subscribeOn(Schedulers.io())
                }?.map {
                    it.results ?: listOf()
                }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ results ->
                    if (list?.adapter == null) {
                        list?.adapter = MovieAdapter(results)
                    } else {
                        val newList = mutableListOf<Movie>().apply {
                            addAll((list.adapter as MovieAdapter).items)
                            addAll(results)
                        }
                        val diffCallback = MovieDiffCallback((list.adapter as MovieAdapter).items, newList)
                        (list.adapter as MovieAdapter).items = newList
                        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter as MovieAdapter)
                    }

                    finishLoading()
                }, this::handleError
                )?.let{
                    disposables.add(it)
                }

        nextPage()
    }

}