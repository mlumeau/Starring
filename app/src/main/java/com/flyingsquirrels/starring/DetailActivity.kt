package com.flyingsquirrels.starring

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.flyingsquirrels.starring.model.TMDBMovie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*



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

    private var posterPath:String = ""
    private var drawPosterOnCreate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        ViewCompat.setTransitionName(poster, EXTRA_IMAGE)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        intent.extras.getByteArray(EXTRA_THUMBNAIL)?.let {
            poster.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }
        if(savedInstanceState?.getBoolean(DRAW_POSTER_ON_CREATE) != null) {
            drawPosterOnCreate = savedInstanceState.getBoolean(DRAW_POSTER_ON_CREATE)
        }


        when(intent.extras?.getString(EXTRA_MEDIA_TYPE)){
            EXTRA_MOVIE -> bindMovie(intent.extras.getParcelable(EXTRA_MOVIE))
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
        posterPath = when(resources.configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT->movie.posterPath.toString()
            Configuration.ORIENTATION_LANDSCAPE->movie.backdropPath.toString()
            else->movie.posterPath.toString()
        }
        collapsing_toolbar.title = movie.title
        if(drawPosterOnCreate || resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setUpPoster()
        }
/*
        collapsing_toolbar.setContentScrimColor(palette.getMutedColor(primary))
        collapsing_toolbar.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark))
        fab.setRippleColor(lightVibrantColor)
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor))
*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                prepareFinish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        collapsing_toolbar.title = ""
        toolbar.title = ""
        supportFinishAfterTransition()
    }

    private fun prepareFinish() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        collapsing_toolbar.title = ""
        toolbar.title = ""
        supportFinishAfterTransition()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(DRAW_POSTER_ON_CREATE,true)
        super.onSaveInstanceState(outState)
    }

}
