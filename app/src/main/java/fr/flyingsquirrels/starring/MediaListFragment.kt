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
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import fr.flyingsquirrels.starring.db.StarringDB
import fr.flyingsquirrels.starring.model.*
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import fr.flyingsquirrels.starring.utils.MovieDiffCallback
import fr.flyingsquirrels.starring.utils.PeopleDiffCallback
import fr.flyingsquirrels.starring.utils.TVShowDiffCallback
import fr.flyingsquirrels.starring.utils.inflate
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.adapter_movies.view.*
import kotlinx.android.synthetic.main.adapter_people.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream



class MediaListFragment : Fragment() {

    companion object {
        const val TYPE_KEY:String="type"
        const val FAV_MOVIE:String="fav_movie"
        const val FAV_TV:String="fav_tv"
        const val FAV_PEOPLE:String="fav_people"

        fun newInstance(args: Bundle?): MediaListFragment {
            val fragment = MediaListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }

    private val tmdb: TMDBRetrofitService by inject()
    private val starringDB: StarringDB by inject()

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

        view.viewTreeObserver.addOnGlobalLayoutListener {

            val spanCount: Int = if(context!=null
                    && (view.width / context!!.resources.displayMetrics.density + 0.5f)>=480){
                4
            }else{
                2
            }
            grid.spanCount = spanCount
        }

        if(type!=null) {

            when {
                type!!.startsWith("tv") -> {

                    val tvListRequest: Observable<TVShowResponse>? = when (type) {
                        TVShowResponse.POPULAR -> tmdb.getPopularTVShows()
                        TVShowResponse.TOP_RATED -> tmdb.getTopRatedTVShows()
                        TVShowResponse.AIRING_TODAY -> tmdb.getAiringTodayTVShows()
                        TVShowResponse.ON_THE_AIR -> tmdb.getOnTheAirTVShows()
                        else -> null
                    }
                    swipe_refresh.setOnRefreshListener {
                        tvListRequest?.bindToLifecycle(this)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                                { response ->
                                    list?.adapter = response?.results?.let { TVAdapter(it) }
                                    finishLoading()
                                },
                                {
                                    finishLoading()
                                }
                        )
                    }
                    tvListRequest?.bindToLifecycle(this)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                            { response ->
                                list?.adapter = response?.results?.let { TVAdapter(it) }
                                finishLoading()
                            },
                            {
                                finishLoading()
                            }
                    )
                }
                type!!.startsWith("movie") -> {

                    val movieListRequest: Observable<MovieResponse>? = when (type) {
                        MovieResponse.POPULAR -> tmdb.getPopularMovies()
                        MovieResponse.TOP_RATED -> tmdb.getTopRatedMovies()
                        MovieResponse.NOW_PLAYING -> tmdb.getNowPlayingMovies()
                        MovieResponse.UPCOMING -> tmdb.getUpcomingMovies()
                        else -> null
                    }

                    swipe_refresh.setOnRefreshListener {
                        movieListRequest?.bindToLifecycle(this)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                                { response ->
                                    list?.adapter = response?.results?.let { FilmsAdapter(it) }
                                    finishLoading()
                                },
                                {
                                    finishLoading()
                                }
                        )
                    }
                    movieListRequest?.bindToLifecycle(this)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                            { response ->
                                list?.adapter = response?.results?.let { FilmsAdapter(it) }
                                finishLoading()
                            },
                            {
                                finishLoading()
                            }
                    )

                }
                type!!.startsWith("people") -> {

                    val peopleListRequest: Observable<PeopleResponse>? = when (type) {
                        PeopleResponse.POPULAR -> tmdb.getPopularPeople()
                        else -> null
                    }

                    swipe_refresh.setOnRefreshListener {
                        peopleListRequest?.bindToLifecycle(this)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                                { response ->
                                    list?.adapter = response?.people?.let { PeopleAdapter(it) }
                                    finishLoading()
                                },
                                {
                                    finishLoading()
                                }
                        )
                    }
                    peopleListRequest?.bindToLifecycle(this)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                            { response ->
                                list?.adapter = response?.people?.let { PeopleAdapter(it) }
                                finishLoading()
                            },
                            {
                                finishLoading()
                            }
                    )

                }
                type!!.startsWith("fav") -> {
                    //Favorites fragment
                    swipe_refresh.isEnabled = false
                    when(type){
                        FAV_MOVIE -> {
                            starringDB.favoritesDao().getFavoriteMovies()
                                    .compose(provider.bindToLifecycle())
                                    .subscribe {
                                        this@MediaListFragment.activity?.runOnUiThread {
                                            if (list?.adapter == null) {
                                                list?.adapter = FilmsAdapter(it)
                                            } else {
                                                val diffCallback = MovieDiffCallback((list.adapter as FilmsAdapter).items, it)
                                                (list.adapter as FilmsAdapter).items = it
                                                DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter)
                                            }

                                            finishLoading()
                                        }
                                    }
                        }
                        FAV_TV -> {
                            starringDB.favoritesDao().getFavoriteTVShows()
                                    .compose(provider.bindToLifecycle())
                                    .subscribe {
                                        this@MediaListFragment.activity?.runOnUiThread {
                                            if (list?.adapter == null) {
                                                list?.adapter = TVAdapter(it)
                                            } else {
                                                val diffCallback = TVShowDiffCallback((list.adapter as TVAdapter).items, it)
                                                (list.adapter as TVAdapter).items = it
                                                DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter)
                                            }

                                            finishLoading()
                                        }
                                    }
                        }
                        FAV_PEOPLE -> {
                            starringDB.favoritesDao().getFavoritePeople()
                                    .compose(provider.bindToLifecycle())
                                    .subscribe {
                                        this@MediaListFragment.activity?.runOnUiThread {
                                            if (list?.adapter == null) {
                                                list?.adapter = PeopleAdapter(it)
                                            } else {
                                                val diffCallback = PeopleDiffCallback((list.adapter as PeopleAdapter).items, it)
                                                (list.adapter as PeopleAdapter).items = it
                                                DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter)
                                            }

                                            finishLoading()
                                        }
                                    }
                        }
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

    inner class TVAdapter(var items: List<TVShow>) : RecyclerView.Adapter<MediaHolder>() {
        override fun onBindViewHolder(holder: MediaHolder, position: Int) = holder.bindTVShow(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder = MediaHolder(parent.inflate(R.layout.adapter_movies))

        override fun getItemCount(): Int = items.size
    }

    inner class FilmsAdapter(var items: List<Movie>) : RecyclerView.Adapter<MediaHolder>() {
        override fun onBindViewHolder(holder: MediaHolder, position: Int) = holder.bindMovie(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder = MediaHolder(parent.inflate(R.layout.adapter_movies))

        override fun getItemCount(): Int = items.size

    }

    inner class PeopleAdapter(var items: List<Person>) : RecyclerView.Adapter<PeopleHolder>() {
        override fun onBindViewHolder(holder: PeopleHolder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleHolder = PeopleHolder(parent.inflate(R.layout.adapter_people_vertical))

        override fun getItemCount(): Int = items.size

    }

    inner class MediaHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindMovie(movie: Movie) {
            bind(DetailActivity.EXTRA_MOVIE,movie, TMDBCONST.POSTER_URL_THUMBNAIL +movie.posterPath)
        }

        fun bindTVShow(tvShow: TVShow) {
            bind(DetailActivity.EXTRA_TV_SHOW,tvShow, TMDBCONST.POSTER_URL_THUMBNAIL +tvShow.posterPath)
        }

        private fun bind(type : String, media : Parcelable, imagePath : String){
            Picasso.get().load(imagePath).placeholder(R.color.grey600).fit().centerCrop().into(itemView.cover)

            this.itemView.setOnClickListener {

                it.transitionName = DetailActivity.EXTRA_IMAGE
                val intent = Intent(it.context, DetailActivity::class.java)

                var options: Bundle? = null
                val extras = Bundle()
                if (this@MediaListFragment.activity != null) {
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MediaListFragment.activity!!, it, DetailActivity.EXTRA_SHARED_POSTER).toBundle()
                }
                if(it.cover.drawable != null && it.cover.drawable is BitmapDrawable) {
                    val b: Bitmap = (itemView.cover.drawable as BitmapDrawable).bitmap
                    val bs = ByteArrayOutputStream()
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                    extras.putByteArray(DetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                }
                extras.putString(DetailActivity.EXTRA_MEDIA_TYPE, type)
                extras.putParcelable(DetailActivity.EXTRA_PAYLOAD, media)

                intent.putExtras(extras)

                it.context.startActivity(intent, options)
            }
        }

    }

    inner class PeopleHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bind(person : Person){
            Picasso.get().load(TMDBCONST.POSTER_URL_THUMBNAIL + person.profilePath).placeholder(R.color.grey600).fit().centerCrop().into(itemView.portrait)
            itemView.name_label.text = person.name

            this.itemView.setOnClickListener {

                it.transitionName = DetailActivity.EXTRA_IMAGE
                val intent = Intent(it.context, DetailActivity::class.java)

                var options: Bundle? = null
                val extras = Bundle()
                if (this@MediaListFragment.activity != null) {
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MediaListFragment.activity!!, it, DetailActivity.EXTRA_SHARED_PEOPLE).toBundle()
                }
                if(it.portrait.drawable != null && it.portrait.drawable is BitmapDrawable) {
                    val b: Bitmap = (itemView.portrait.drawable as BitmapDrawable).bitmap
                    val bs = ByteArrayOutputStream()
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                    extras.putByteArray(DetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                }
                extras.putString(DetailActivity.EXTRA_MEDIA_TYPE, DetailActivity.EXTRA_PERSON)
                extras.putParcelable(DetailActivity.EXTRA_PAYLOAD, person)

                intent.putExtras(extras)
                it.context.startActivity(intent, options)

            }
        }

    }
}