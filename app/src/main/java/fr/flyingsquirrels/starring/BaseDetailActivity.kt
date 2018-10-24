package fr.flyingsquirrels.starring

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import fr.flyingsquirrels.starring.db.StarringDB
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.ext.android.inject
import timber.log.Timber


/**
 * Created by mlumeau on 02/03/2018.
 */

abstract class BaseDetailActivity<T> : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE = "image"
        const val EXTRA_PAYLOAD = "data"
        const val EXTRA_THUMBNAIL = "thumbnail"
        const val EXTRA_SHARED_POSTER = "poster"
        const val EXTRA_SHARED_NAV = "nav"
        const val EXTRA_NAV_ITEM = "nav_item"

        protected const val DRAW_POSTER_ON_CREATE = "draw_poster_on_create"
    }

    protected val tmdb: TMDBRetrofitService by inject()
    protected val starringDB: StarringDB by inject()

    protected var posterPath: String = ""
    protected var backdropPath: String = ""
    protected var drawPosterOnCreate = false
    protected var isInFavorites = false

    protected var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        ViewCompat.setTransitionName(nav, EXTRA_SHARED_NAV)

        intent.extras?.getByteArray(EXTRA_THUMBNAIL)?.let { byteArray ->
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            poster.setImageBitmap(bitmap)
            Palette.from(bitmap).generate { palette ->

                palette?.let{

                    val dominantColor = it.getDominantColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    val mutedColor = it.getMutedColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    val vibrantColor = it.getVibrantColor(ContextCompat.getColor(this, R.color.colorAccent))
                    val lightVibrantColor = it.getLightVibrantColor(ContextCompat.getColor(this, R.color.colorAccent))

                    collapsing_toolbar.setBackgroundColor(dominantColor)
                    collapsing_toolbar.setContentScrimColor(mutedColor)
                    collapsing_toolbar.setStatusBarScrimColor(mutedColor)
                    topSlidingPanel.setBackgroundColor(mutedColor)
                    bottomSlidingPanel.setBackgroundColor(mutedColor)
                    fab.backgroundTintList = ColorStateList.valueOf(vibrantColor)
                    fab.rippleColor = lightVibrantColor
                    trailer_label.setTextColor(vibrantColor)
                }
            }

        }

        intent.extras?.getInt(EXTRA_NAV_ITEM)?.let{
            nav.selectedItemId = it
        }

        if (savedInstanceState?.getBoolean(DRAW_POSTER_ON_CREATE) != null) {
            drawPosterOnCreate = savedInstanceState.getBoolean(DRAW_POSTER_ON_CREATE)
        }


        ViewCompat.setTransitionName(poster, EXTRA_SHARED_POSTER)


        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
            title_bar.alpha = 1.0f - Math.abs(i / appBarLayout.totalScrollRange.toFloat())
        })

        nav.setOnNavigationItemSelectedListener {
            val intent = Intent(this,MainActivity::class.java)

            intent.putExtra(MainActivity.SELECTED_TAB, it.itemId)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, nav, EXTRA_SHARED_NAV).toBundle()

            startActivity(intent, options)
            Handler().postDelayed({
                finishAffinity()
            },2000)
            false
        }

    }


    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        setUpPoster()
    }

    protected fun setUpPoster() {
        Picasso.get().load(TMDBCONST.POSTER_URL_THUMBNAIL + posterPath).placeholder(poster.drawable).fit().centerCrop().into(poster)
        Picasso.get().load(TMDBCONST.POSTER_URL_LARGE + backdropPath).fit().centerCrop().into(backdrop)

    }

    protected abstract fun bindData(data: T)


    protected open fun saveAsFavorite(data: T){
        isInFavorites = true
        fab.setImageResource(R.drawable.ic_star_black_24dp)
    }
    protected open fun removeFromFavorites(data: T){
        isInFavorites = false
        fab.setImageResource(R.drawable.ic_star_border_black_24dp)

    }


    protected open fun viewImages(view: ImageView, data: T){
        view.transitionName = ImagesActivity.EXTRA_IMAGE
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


    protected fun handleError(t: Throwable){
        Timber.e(t)
    }

    override fun onBackPressed() {
        prepareFinish()
    }

    private fun prepareFinish() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        fab.systemUiVisibility = View.GONE
        supportFinishAfterTransition()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(DRAW_POSTER_ON_CREATE,true)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
