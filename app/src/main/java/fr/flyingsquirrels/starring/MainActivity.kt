package fr.flyingsquirrels.starring

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import fr.flyingsquirrels.starring.db.StarringDB
import fr.flyingsquirrels.starring.model.TMDBMovie
import fr.flyingsquirrels.starring.model.TMDBMovieResponse
import fr.flyingsquirrels.starring.model.TMDBTVShowResponse
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import fr.flyingsquirrels.starring.utils.AppbarElevationOffsetListener
import fr.flyingsquirrels.starring.utils.TMDBMovieDiffCallback
import io.reactivex.disposables.Disposable
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

        setSupportActionBar(toolbar)

        view_pager.adapter = MoviesPagerAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.addOnTabSelectedListener(
                object : TabLayout.ViewPagerOnTabSelectedListener(view_pager) {

                    override fun onTabSelected(tab: TabLayout.Tab) {
                        super.onTabSelected(tab)
                        if(tab.position==0){
                            val ta = this@MainActivity.obtainStyledAttributes(kotlin.intArrayOf(R.attr.tabSelectedTextColor))
                            val selectedColor = ta.getResourceId(0, android.R.color.black)
                            ta.recycle()
                            tab.icon?.setColorFilter(ContextCompat.getColor(this@MainActivity, selectedColor), PorterDuff.Mode.SRC_IN)
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        super.onTabUnselected(tab)
                        if(tab?.position ==0) {
                            tab.icon?.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.grey600), PorterDuff.Mode.SRC_IN)
                        }
                    }
                }
        )

        appbar.addOnOffsetChangedListener(AppbarElevationOffsetListener())

        tab_layout.getTabAt(0)?.icon = getDrawable(R.drawable.ic_star_black_24dp)
        view_pager.currentItem = 1

    }

    inner class MoviesPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment {

            val args = Bundle()
            val type = when(position){
                1 -> TMDBMovieResponse.NOW_PLAYING
                2 -> TMDBMovieResponse.UPCOMING
                3 -> TMDBMovieResponse.POPULAR
                4 -> TMDBMovieResponse.TOP_RATED
                else -> null
            }

            args.putString(MediaListFragment.TYPE_KEY,type)

            return MediaListFragment.newInstance(args)
        }

        override fun getCount() = 5

        override fun getPageTitle(position: Int): String {
            return when(position){
                1 -> this@MainActivity.getString(R.string.now_playing)
                2 -> this@MainActivity.getString(R.string.upcoming)
                3 -> this@MainActivity.getString(R.string.popular)
                4 -> this@MainActivity.getString(R.string.top_rated)
                else -> ""
            }
        }
    }

    inner class TVShowsPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment {

            val args = Bundle()
            val type = when(position){
                1 -> TMDBTVShowResponse.AIRING_TODAY
                2 -> TMDBTVShowResponse.ON_THE_AIR
                3 -> TMDBMovieResponse.POPULAR
                4 -> TMDBMovieResponse.TOP_RATED
                else -> null
            }

            args.putString(MediaListFragment.TYPE_KEY,type)

            return MediaListFragment.newInstance(args)
        }

        override fun getCount() = 5

        override fun getPageTitle(position: Int): String {
            return when(position){
                1 -> this@MainActivity.getString(R.string.airing_today)
                2 -> this@MainActivity.getString(R.string.on_the_air)
                3 -> this@MainActivity.getString(R.string.popular)
                4 -> this@MainActivity.getString(R.string.top_rated)
                else -> ""
            }
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
    val starringDB: StarringDB by inject()

    var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = arguments?.getString(TYPE_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    private var favSubscription: Disposable? = null

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

            val movieListRequest: Call<TMDBMovieResponse>? = when (type) {
                TMDBMovieResponse.POPULAR -> tmdb.getPopularMovies()
                TMDBMovieResponse.TOP_RATED -> tmdb.getTopRatedMovies()
                TMDBMovieResponse.NOW_PLAYING -> tmdb.getNowPlayingMovies()
                TMDBMovieResponse.UPCOMING -> tmdb.getUpcomingMovies()
                else -> null
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
        }else{
            //Favorites fragment
            swipe_refresh.isEnabled = false
            favSubscription = starringDB.favoritesDao().getFavoriteMovies().subscribe({
                this@MediaListFragment.activity?.runOnUiThread({
                    if(list?.adapter==null){
                        list?.adapter = FilmsAdapter(it)
                    }else{
                        val diffCallback = TMDBMovieDiffCallback((list.adapter as FilmsAdapter).items,it)
                        (list.adapter as FilmsAdapter).items = it
                        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(list.adapter)
                    }
                })
            })

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if(favSubscription !=null){
            favSubscription?.dispose()
        }
    }

    inner class FilmsAdapter(var items: List<TMDBMovie>) : RecyclerView.Adapter<FilmsAdapter.Holder>() {
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