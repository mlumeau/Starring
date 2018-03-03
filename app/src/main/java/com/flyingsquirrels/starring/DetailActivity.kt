package com.flyingsquirrels.starring

import android.content.res.Configuration
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        ViewCompat.setTransitionName(findViewById(R.id.appbar), EXTRA_IMAGE)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        when(intent.extras?.getString(EXTRA_MEDIA_TYPE)){
            EXTRA_MOVIE -> bindMovie(intent.extras.getParcelable(EXTRA_MOVIE))
        }

    }


    fun bindMovie(movie: TMDBMovie){
        val posterPath = when(resources.configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT->movie.posterPath.toString()
            Configuration.ORIENTATION_LANDSCAPE->movie.backdropPath.toString()
            else->movie.posterPath.toString()
        }
        Picasso.with(this).load(posterPath).placeholder(R.color.material_grey_600).fit().centerCrop().into(poster)

        collapsing_toolbar.title = movie.title
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


}
