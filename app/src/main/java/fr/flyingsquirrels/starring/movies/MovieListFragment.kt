package fr.flyingsquirrels.starring.movies

import android.os.Bundle
import android.view.View
import fr.flyingsquirrels.starring.BaseListFragment
import fr.flyingsquirrels.starring.model.MovieResponse
import fr.flyingsquirrels.starring.movies.view.MovieAdapter
import fr.flyingsquirrels.starring.movies.viewmodel.MovieListViewModel
import io.reactivex.Single
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

        val movieListRequest: Single<MovieResponse>? = when (arguments?.get(BaseListFragment.TYPE_KEY)) {
            MovieResponse.POPULAR -> vm.getPopularMovies()
            MovieResponse.TOP_RATED -> vm.getTopRatedMovies()
            MovieResponse.NOW_PLAYING -> vm.getNowPlayingMovies()
            MovieResponse.UPCOMING -> vm.getUpcomingMovies()
            else -> null
        }

        swipe_refresh.setOnRefreshListener {
            movieListRequest?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                    { response ->
                        list?.adapter = response?.results?.let { MovieAdapter(it) }
                        finishLoading()
                    }, this::handleError
            )?.let{
                disposables.add(it)
            }
        }
        movieListRequest?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                { response ->
                    list?.adapter = response?.results?.let { MovieAdapter(it) }
                    finishLoading()
                }, this::handleError
        )?.let{
            disposables.add(it)
        }
    }
}