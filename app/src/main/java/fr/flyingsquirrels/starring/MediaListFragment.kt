package fr.flyingsquirrels.starring

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import fr.flyingsquirrels.starring.db.StarringDB
import fr.flyingsquirrels.starring.model.TMDBMovie
import fr.flyingsquirrels.starring.model.TMDBMovieResponse
import fr.flyingsquirrels.starring.model.TMDBTVShow
import fr.flyingsquirrels.starring.model.TMDBTVShowResponse
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import fr.flyingsquirrels.starring.network.TMDB_CONST
import fr.flyingsquirrels.starring.utils.TMDBMovieDiffCallback
import fr.flyingsquirrels.starring.utils.TMDBTVShowDiffCallback
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.adapter_movies.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream



class MediaListFragment : Fragment() {

    companion object {
        const val TYPE_KEY:String="type"
        const val FAV_MOVIE:String="fav_movie"
        const val FAV_TV:String="fav_tv"

        fun newInstance(args: Bundle?): MediaListFragment {
            val fragment = MediaListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }

    val tmdb: TMDBRetrofitService by inject()
    val starringDB: StarringDB by inject()

    var type: String? = null

    private val provider = AndroidLifecycle.createLifecycleProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(parentFragment != null && parentFragment is TabsFragment)
            onScrollChangeListener = (parentFragment as TabsFragment).onScrollListener

        type = arguments?.getString(TYPE_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    private var onScrollChangeListener: RecyclerView.OnScrollListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val grid = GridLayoutManager(context, 2)
        list.layoutManager = grid

        view.viewTreeObserver.addOnGlobalLayoutListener({

            val spanCount: Int = if(context!=null
                    && (view.width / context!!.resources.displayMetrics.density + 0.5f)>=480){
                4
            }else{
                2
            }
            grid.spanCount = spanCount
        })

        if(type!=null) {

            if (type!!.startsWith("tv")) {

                val tvListRequest: Call<TMDBTVShowResponse>? = when (type) {
                    TMDBTVShowResponse.POPULAR -> tmdb.getPopularTVShows()
                    TMDBTVShowResponse.TOP_RATED -> tmdb.getTopRatedTVShows()
                    TMDBTVShowResponse.AIRING_TODAY -> tmdb.getAiringTodayTVShows()
                    TMDBTVShowResponse.ON_THE_AIR -> tmdb.getOnTheAirTVShows()
                    else -> null
                }

                val tvRequestCallback = object : MediaCallback<TMDBTVShowResponse>() {

                    override fun onResponse(call: Call<TMDBTVShowResponse>?, response: Response<TMDBTVShowResponse>?) {
                        if (response != null) {
                            val tmdbResponse = response.body()
                            list?.adapter = tmdbResponse?.results?.let { TVAdapter(it) }
                            finishLoading()
                        }
                    }
                }

                swipe_refresh.setOnRefreshListener {
                    tvListRequest.let {
                        it?.clone()?.enqueue(tvRequestCallback)
                    }
                }
                tvListRequest?.enqueue(tvRequestCallback)
            } else if (type!!.startsWith("movie")) {

                val movieListRequest: Call<TMDBMovieResponse>? = when (type) {
                    TMDBMovieResponse.POPULAR -> tmdb.getPopularMovies()
                    TMDBMovieResponse.TOP_RATED -> tmdb.getTopRatedMovies()
                    TMDBMovieResponse.NOW_PLAYING -> tmdb.getNowPlayingMovies()
                    TMDBMovieResponse.UPCOMING -> tmdb.getUpcomingMovies()
                    else -> null
                }

                val movieRequestCallback = object : MediaCallback<TMDBMovieResponse>() {

                    override fun onResponse(call: Call<TMDBMovieResponse>?, response: Response<TMDBMovieResponse>?) {
                        if (response != null) {
                            val tmdbResponse = response.body()
                            list?.adapter = tmdbResponse?.results?.let { FilmsAdapter(it) }
                            finishLoading()
                        }
                    }
                }

                swipe_refresh.setOnRefreshListener {
                    movieListRequest.let {
                        it?.clone()?.enqueue(movieRequestCallback)
                    }
                }

                movieListRequest?.enqueue(movieRequestCallback)

            } else if(type!!.startsWith("fav")) {
                //Favorites fragment
                swipe_refresh.isEnabled = false
                when(type){
                    FAV_MOVIE -> {
                        starringDB.favoritesDao().getFavoriteMovies()
                                .compose(provider.bindToLifecycle())
                                .subscribe({
                            this@MediaListFragment.activity?.runOnUiThread({
                                if (list?.adapter == null) {
                                    list?.adapter = FilmsAdapter(it)
                                } else {
                                    val diffCallback = TMDBMovieDiffCallback((list.adapter as FilmsAdapter).items, it)
                                    (list.adapter as FilmsAdapter).items = it
                                    DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter)
                                }

                                finishLoading()
                            })
                        })
                    }
                    FAV_TV -> {
                        starringDB.favoritesDao().getFavoriteTVShows()
                                .compose(provider.bindToLifecycle())
                                .subscribe({
                            this@MediaListFragment.activity?.runOnUiThread({
                                if (list?.adapter == null) {
                                    list?.adapter = TVAdapter(it)
                                } else {
                                    val diffCallback = TMDBTVShowDiffCallback((list.adapter as TVAdapter).items, it)
                                    (list.adapter as TVAdapter).items = it
                                    DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter)
                                }

                                finishLoading()
                            })
                        })
                    }
                }

            }

        }

        list.addOnScrollListener(onScrollChangeListener)

        loading.visibility = View.VISIBLE
    }

    abstract inner class MediaCallback<T> : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            Log.e("", "error", t)
            finishLoading()
        }
    }

    private fun finishLoading() {
        swipe_refresh?.isRefreshing = false
        loading?.visibility = View.GONE
    }

    inner class TVAdapter(var items: List<TMDBTVShow>) : RecyclerView.Adapter<Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bindTVShow(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_movies))

        override fun getItemCount(): Int = items.size
    }

    inner class FilmsAdapter(var items: List<TMDBMovie>) : RecyclerView.Adapter<Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bindMovie(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_movies))

        override fun getItemCount(): Int = items.size

    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindMovie(movie: TMDBMovie) {
            bind(DetailActivity.EXTRA_MOVIE,movie, TMDB_CONST.POSTER_URL_THUMBNAIL +movie.posterPath)
        }

        fun bindTVShow(movie: TMDBTVShow) {
            bind(DetailActivity.EXTRA_TV_SHOW,movie, TMDB_CONST.POSTER_URL_THUMBNAIL +movie.posterPath)
        }

        private fun bind(type : String, media : Parcelable, imagePath : String){
            Picasso.with(itemView.context).load(imagePath).placeholder(R.color.material_grey_600).fit().centerCrop().into(itemView.cover)

            //todo implement tv details
            if(media is TMDBMovie) {
                this.itemView.setOnClickListener {

                    it.transitionName = DetailActivity.EXTRA_IMAGE
                    val intent = Intent(it.context, DetailActivity::class.java)

                    var options: Bundle? = Bundle()
                    val extras = Bundle()
                    if (this@MediaListFragment.activity != null) {
                        options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MediaListFragment.activity!!, it, DetailActivity.EXTRA_IMAGE).toBundle()
                    }

                    val b: Bitmap = (itemView.cover.drawable as BitmapDrawable).bitmap
                    val bs = ByteArrayOutputStream()
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                    extras.putByteArray(DetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                    extras.putString(DetailActivity.EXTRA_MEDIA_TYPE, type)
                    extras.putParcelable(DetailActivity.EXTRA_MEDIA, media)

                    intent.putExtras(extras)

                    it.context.startActivity(intent, options)
                }
            }
        }

    }
}