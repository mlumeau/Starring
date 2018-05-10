package fr.flyingsquirrels.starring

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
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
import com.squareup.picasso.Picasso
import fr.flyingsquirrels.starring.model.TMDBMovie
import fr.flyingsquirrels.starring.model.TMDBMovieResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_movies.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


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
            lateinit var type:String

            when(position){
                0 -> type= TMDBMovieResponse.NOW_PLAYING
                1 -> type=TMDBMovieResponse.UPCOMING
                2 -> type=TMDBMovieResponse.POPULAR
                3 -> type=TMDBMovieResponse.TOP_RATED
            }

            args.putString(MediaListFragment.TYPE_KEY,type)

            return MediaListFragment.newInstance(args)
        }

        override fun getCount() = 4

        override fun getPageTitle(position: Int) = when(position){
            0 -> this@MainActivity.getString(R.string.now_playing)
            1 -> this@MainActivity.getString(R.string.upcoming)
            2 -> this@MainActivity.getString(R.string.popular)
            3 -> this@MainActivity.getString(R.string.top_rated)
            else -> ""
        }
    }

}



class MediaListFragment : Fragment() {

    companion object {
        const val TYPE_KEY:String="type"

        fun newInstance(args: Bundle?): MediaListFragment{
            val fragment = MediaListFragment()

            fragment.arguments = args
            if (args == null) {
                fragment.arguments = Bundle()
            }

            return fragment
        }
    }

    val tmdb: TMDBRetrofitService by inject()

    var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = arguments?.getString(TYPE_KEY)
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

        val movieListRequest:Call<TMDBMovieResponse>? = when(type){
            TMDBMovieResponse.POPULAR -> tmdb.getPopularMovies()
            TMDBMovieResponse.TOP_RATED -> tmdb.getTopRatedMovies()
            TMDBMovieResponse.NOW_PLAYING -> tmdb.getNowPlayingMovies()
            TMDBMovieResponse.UPCOMING -> tmdb.getUpcomingMovies()
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
                    list?.adapter = tmdbResponse?.results?.let { FilmsAdapter(it) }
                    finishLoading()
                }
            }

            private fun finishLoading() {
                swipe_refresh?.isRefreshing = false
                loading?.visibility = View.GONE
            }


        }

        swipe_refresh.setOnRefreshListener { movieListRequest?.clone()?.enqueue(requestCallback) }

        movieListRequest?.enqueue(requestCallback)
        loading.visibility = View.VISIBLE

    }

    inner class FilmsAdapter(private val items: List<TMDBMovie>) : RecyclerView.Adapter<FilmsAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_movies))

        override fun getItemCount(): Int = items.size

        inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(movie: TMDBMovie) {
                Picasso.with(itemView.context).load(TMDBMovie.POSTER_URL_THUMBNAIL+movie.posterPath).placeholder(R.color.material_grey_600).fit().centerCrop().into(itemView.cover)
                this.itemView.setOnClickListener {

                    it.transitionName = fr.flyingsquirrels.starring.DetailActivity.EXTRA_IMAGE
                    val intent = Intent(it.context, fr.flyingsquirrels.starring.DetailActivity::class.java)

                    var options: Bundle? = Bundle()
                    val extras = Bundle()
                    if(this@MediaListFragment.activity!=null) {
                        options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MediaListFragment.activity!!, it, fr.flyingsquirrels.starring.DetailActivity.EXTRA_IMAGE).toBundle()
                    }

                    val b: Bitmap = (itemView.cover.drawable as BitmapDrawable).bitmap
                    val bs = ByteArrayOutputStream()
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                    extras.putByteArray(fr.flyingsquirrels.starring.DetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                    extras.putParcelable(fr.flyingsquirrels.starring.DetailActivity.EXTRA_MOVIE,movie)
                    extras.putString(fr.flyingsquirrels.starring.DetailActivity.EXTRA_MEDIA_TYPE, fr.flyingsquirrels.starring.DetailActivity.EXTRA_MOVIE)

                    intent.putExtras(extras)

                    it.context.startActivity(intent,options)
                }
            }

        }

    }
}




fun ViewGroup.inflate(adapter_layout: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(adapter_layout,this, attachToRoot)
}