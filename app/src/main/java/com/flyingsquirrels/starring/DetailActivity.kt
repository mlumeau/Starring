package com.flyingsquirrels.starring

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.flyingsquirrels.starring.model.CastItem
import com.flyingsquirrels.starring.model.TMDBMovie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.adapter_movies.view.*
import kotlinx.android.synthetic.main.adapter_people.view.*
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


/**
 * Created by mlumeau on 02/03/2018.
 */

class DetailActivity : AppCompatActivity(){

    companion object {
        const val EXTRA_IMAGE = "image"
        const val EXTRA_MOVIE = "movie"
        const val EXTRA_MEDIA_TYPE = "type"
        const val EXTRA_THUMBNAIL = "thumbnail"

        private const val DRAW_POSTER_ON_CREATE = "draw_poster_on_create"
    }

    val tmdb: TMDBRetrofitService by inject()
    private var posterPath:String = ""
    private var drawPosterOnCreate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        ViewCompat.setTransitionName(poster, EXTRA_IMAGE)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        intent.extras.getByteArray(EXTRA_THUMBNAIL)?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            poster.setImageBitmap(bitmap)
            Palette.from(bitmap).generate({

                collapsing_toolbar.setContentScrimColor(it.getVibrantColor(ContextCompat.getColor(this,R.color.colorPrimary)))
                collapsing_toolbar.setBackgroundColor(it.getDominantColor(ContextCompat.getColor(this,R.color.colorPrimary)))
                topSlidingPanel.setBackgroundColor(it.getVibrantColor(ContextCompat.getColor(this,R.color.colorPrimary)))
                bottomSlidingPanel.setBackgroundColor(it.getVibrantColor(ContextCompat.getColor(this,R.color.colorPrimary)))
                title_bar.setBackgroundColor(it.getVibrantColor(ContextCompat.getColor(this,R.color.colorPrimary)))
                collapsing_toolbar.setStatusBarScrimColor(it.getVibrantColor(ContextCompat.getColor(this,R.color.colorPrimaryDark)))
                fab.rippleColor = it.getLightMutedColor(ContextCompat.getColor(this,R.color.colorAccent))
                fab.backgroundTintList = ColorStateList.valueOf(it.getMutedColor(ContextCompat.getColor(this,R.color.colorAccent)))
            })

        }



        if(savedInstanceState?.getBoolean(DRAW_POSTER_ON_CREATE) != null) {
            drawPosterOnCreate = savedInstanceState.getBoolean(DRAW_POSTER_ON_CREATE)
        }


        when(intent.extras?.getString(EXTRA_MEDIA_TYPE)){
            EXTRA_MOVIE ->{
                var movie:TMDBMovie? = intent.extras.getParcelable(EXTRA_MOVIE)
                movie?.let {intentMovie ->
                    bindMovie(intentMovie)

                    intentMovie.id?.let { id ->
                        tmdb.getMovieDetails(id).enqueue(object : Callback<TMDBMovie> {
                            override fun onFailure(call: Call<TMDBMovie>?, t: Throwable?) {

                            }

                            override fun onResponse(call: Call<TMDBMovie>?, response: Response<TMDBMovie>?) {
                                response?.body()?.let{responseMovie ->
                                    movie = responseMovie
                                    bindMovie(responseMovie)
                                }
                            }
                        })
                    }

                }

            }
        }


    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setUpPoster()
        }
    }

    private fun setUpPoster() {
        Picasso.with(this).load(TMDBMovie.POSTER_URL_LARGE + posterPath).placeholder(poster.drawable).fit().centerCrop().into(poster)
    }


    private fun bindMovie(movie: TMDBMovie){
        
        //Poster
        posterPath = when(resources.configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT->movie.posterPath.toString()
            Configuration.ORIENTATION_LANDSCAPE->movie.backdropPath.toString()
            else->movie.posterPath.toString()
        }
        
        //Header
        collapsing_toolbar.title = movie.title
        if(drawPosterOnCreate || resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setUpPoster()
        }

        val year = movie.releaseDate?.substring(0,4)
        var info=""

        year?.let{
            info+=year
        }
        movie.runtime?.let{
            if(!TextUtils.isEmpty(info)){
                info+=" · "
            }
            info+= "$it min"
        }
        movie.voteAverage?.let{
            if(!TextUtils.isEmpty(info)){
                info+=" · "
            }
            info+= "$it/10"
        }

        title_info_label.text = info

        
        //Directors
        var directedByString:String? = null
        movie.getDirectors()?.forEachIndexed { i, director ->
            if(i==0){
                directedByString = getString(R.string.directed_by) + " "
            }else{
                directedByString += ", "
            }

            directedByString+= director?.name
        }
        if(directedByString==null){
            directed_by.visibility = View.GONE
        }else{
            directed_by.visibility = View.VISIBLE
            directed_by_label.text = directedByString
        }
        
        //Countries
        var countriesString:String? = null
        movie.productionCountries?.forEachIndexed { i, country ->
            if(i==0){
                countriesString = ""
            }else{
                countriesString += ", "
            }

            countriesString+= country?.name
        }
        if(countriesString==null){
            country.visibility = View.GONE
        }else{
            country.visibility = View.VISIBLE
            country_label.text = countriesString
        }
        
        //Genres
        var genresString:String? = null
        movie.genres?.forEachIndexed { i, genre ->
            if(i==0){
                genresString = ""
            }else{
                genresString += ", "
            }

            genresString+= genre?.name
        }
        if(genresString==null){
            genre.visibility = View.GONE
        }else{
            genre.visibility = View.VISIBLE
            genre_label.text = genresString
        }

        //Cast
        if(movie.credits?.cast != null && movie.credits.cast.isNotEmpty()){
            starring.visibility = View.VISIBLE
            starring_list.adapter = CastAdapter(movie.credits.cast.filterNotNull())
        }else{
            starring.visibility = View.GONE
        }


        //Plot
        if(movie.overview.isNullOrEmpty()){
            plot.visibility = View.GONE
        }else{
            plot.visibility = View.VISIBLE
            plot_label.text = movie.overview
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                prepareFinish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        prepareFinish()
    }

    private fun prepareFinish() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        fab.visibility = View.GONE
        supportFinishAfterTransition()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(DRAW_POSTER_ON_CREATE,true)
        super.onSaveInstanceState(outState)
    }

    inner class CastAdapter(private val items: List<CastItem>) : RecyclerView.Adapter<CastAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_people))

        override fun getItemCount(): Int = if(items.size >= 8) 8 else items.size

        inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(castItem: CastItem) {
                Picasso.with(itemView.context).load(TMDBMovie.POSTER_URL_THUMBNAIL + castItem.profilePath).placeholder(R.color.material_grey_600).fit().centerInside().into(itemView.portrait)
                itemView.name_label.text = castItem.name

                this.itemView.setOnClickListener {
                    //TODO: implement detail activity UI & behaviour for cast
                }
            }

        }
    }
}
