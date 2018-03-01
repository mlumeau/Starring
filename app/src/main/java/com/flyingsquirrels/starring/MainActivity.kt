package com.flyingsquirrels.starring

import android.opengl.Visibility
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import dagger.Component
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_movies.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.adapter = MoviesPagerAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(view_pager)

    }

    inner class MoviesPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment {

            val args = Bundle()
            lateinit var type:MovieLists

            when(position){
                0 -> type=MovieLists.NOW_PLAYING
                1 -> type=MovieLists.UPCOMING
                2 -> type=MovieLists.POPULAR
                3 -> type=MovieLists.TOP_RATED
            }

            args.putParcelable(MediaListFragment.TYPE_KEY,type)

            return MediaListFragment.newInstance(args)
        }

        override fun getCount() = 4

        override fun getPageTitle(position: Int) = when(position){
            0 -> this@MainActivity.getString(R.string.now_playing)
            1 -> this@MainActivity.getString(R.string.upcoming)
            2 -> this@MainActivity.getString(R.string.top_rated)
            3 -> this@MainActivity.getString(R.string.popular)
            else -> ""
        }
    }

}



class MediaListFragment : Fragment() {

    companion object {
        const val TYPE_KEY:String="type"

        fun newInstance(args: Bundle?): MediaListFragment{
            var args = args
            val fragment = MediaListFragment()

            if (args == null) {
                args = Bundle()
            }
            fragment.arguments = args

            return fragment
        }
    }

    @Inject
    lateinit var tmdb: TMDBRetrofitService

    var type: MovieLists? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as StarringApp).network.inject(this)
        type = arguments?.get(TYPE_KEY) as MovieLists
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

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

        val movieListRequest = when(type){
            MovieLists.POPULAR -> tmdb.getPopularMovies()
            MovieLists.TOP_RATED -> tmdb.getTopRatedMovies()
            MovieLists.NOW_PLAYING -> tmdb.getNowPlayingMovies()
            MovieLists.UPCOMING -> tmdb.getUpcomingMovies()
            else->null
        }


        val requestCallback = object : Callback<TMDBMovieResponse> {
            override fun onFailure(call: Call<TMDBMovieResponse>?, t: Throwable?) {
                Log.e("", "error", t)
                finishLoading()
            }

            override fun onResponse(call: Call<TMDBMovieResponse>?, response: Response<TMDBMovieResponse>?) {
                if (response != null) {
                    val tmdbResponse = response.body()
                    list.adapter = tmdbResponse?.results?.let { FilmsAdapter(it) }
                    finishLoading()
                }
            }

            private fun finishLoading() {
                swipe_refresh.isRefreshing = false
                loading.visibility = View.GONE
            }


        }

        swipe_refresh.setOnRefreshListener { movieListRequest?.clone()?.enqueue(requestCallback) }

        movieListRequest?.enqueue(requestCallback)
        loading.visibility = View.VISIBLE

    }
}


class FilmsAdapter(private val items: List<TMDBMovie>) : RecyclerView.Adapter<FilmsAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_movies))

    override fun getItemCount(): Int = items.size

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: TMDBMovie) {
            Picasso.with(itemView.context).load("https://image.tmdb.org/t/p/w300"+movie.posterPath).placeholder(R.color.material_grey_600).fit().centerCrop().into(itemView.cover)
        }

    }

}

private fun ViewGroup.inflate(adapter_layout: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(adapter_layout,this, attachToRoot)
}


data class TMDBMovieResponse(@field:SerializedName("results")  var results: List<TMDBMovie>)
data class TMDBMovie(@field:SerializedName("poster_path")  var posterPath: String,
                     @field:SerializedName("adult") var isAdult: Boolean,
                     @field:SerializedName("overview") var overview: String,
                     @field:SerializedName("release_date") var releaseDate: String,
                     @field:SerializedName("genre_ids") var genreIds: List<Int>,
                     @field:SerializedName("id") var id: Int,
                     @field:SerializedName("original_title") var originalTitle: String,
                     @field:SerializedName("original_language") var originalLanguage: String,
                     @field:SerializedName("title") var title: String,
                     @field:SerializedName("backdrop_path") var backdropPath: String,
                     @field:SerializedName("popularity") var popularity: Double,
                     @field:SerializedName("vote_count") var voteCount: Int?,
                     @field:SerializedName("video") var video: Boolean?,
                     @field:SerializedName("vote_average") var voteAverage: Double)

enum class MovieLists : Parcelable{
    TOP_RATED,
    POPULAR,
    NOW_PLAYING,
    UPCOMING;


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieLists> {
        override fun createFromParcel(parcel: Parcel): MovieLists {
            return MovieLists.valueOf(parcel.readString())
        }

        override fun newArray(size: Int): Array<MovieLists?> {
            return arrayOfNulls(size)
        }
    }

}


@Singleton
@Component(modules = [(NetworkModule::class)])
interface NetworkComponent {
    fun inject(fragment: MediaListFragment)
}