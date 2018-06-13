package fr.flyingsquirrels.starring

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import fr.flyingsquirrels.starring.db.StarringDB
import fr.flyingsquirrels.starring.model.CastItem
import fr.flyingsquirrels.starring.model.Season
import fr.flyingsquirrels.starring.model.TMDBMovie
import fr.flyingsquirrels.starring.model.TMDBTVShow
import fr.flyingsquirrels.starring.network.TMDBRetrofitService
import fr.flyingsquirrels.starring.network.TMDB_CONST
import fr.flyingsquirrels.starring.utils.inflate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.adapter_people.view.*
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


/**
 * Created by mlumeau on 02/03/2018.
 */

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE = "image"
        const val EXTRA_MEDIA = "media"
        const val EXTRA_MOVIE = "movie"
        const val EXTRA_TV_SHOW = "tv_show"
        const val EXTRA_TV_SHOW_SEASON = "tv_show_season"
        const val EXTRA_TV_SHOW_EPISODE = "tv_show_episode"
        const val EXTRA_MEDIA_TYPE = "type"
        const val EXTRA_THUMBNAIL = "thumbnail"

        private const val DRAW_POSTER_ON_CREATE = "draw_poster_on_create"
    }

    private val tmdb: TMDBRetrofitService by inject()
    private val starringDB: StarringDB by inject()

    private var posterPath: String = ""
    private var backdropPath: String = ""
    private var drawPosterOnCreate = false
    private var isInFavorites = false

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
            })

        }



        if (savedInstanceState?.getBoolean(DRAW_POSTER_ON_CREATE) != null) {
            drawPosterOnCreate = savedInstanceState.getBoolean(DRAW_POSTER_ON_CREATE)
        }


        when (intent.extras?.getString(EXTRA_MEDIA_TYPE)) {
            EXTRA_MEDIA -> {
                val movie: TMDBMovie? = intent.extras.getParcelable(EXTRA_MEDIA)
                movie?.let { intentMovie ->
                    bindMovie(intentMovie)

                    Schedulers.io().scheduleDirect({
                        starringDB.favoritesDao().getFavoriteMovieWithId(intentMovie.id).subscribe({
                            runOnUiThread {
                                isInFavorites = true
                                fab.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
                            }
                        })
                    })
                    intentMovie.id?.let { id ->
                        tmdb.getMovieDetails(id).enqueue(object : Callback<TMDBMovie> {
                            override fun onFailure(call: Call<TMDBMovie>?, t: Throwable?) {

                            }

                            override fun onResponse(call: Call<TMDBMovie>?, response: Response<TMDBMovie>?) {
                                response?.body()?.let { responseMovie ->
                                    bindMovie(responseMovie)
                                }
                            }
                        })

                    }


                }

            }

            EXTRA_TV_SHOW -> {
                val tvShow: TMDBTVShow? = intent.extras.getParcelable(EXTRA_MEDIA)
                tvShow?.let { intentShow ->
                    bindTVShow(intentShow)

                    Schedulers.io().scheduleDirect({
                        starringDB.favoritesDao().getFavoriteTVShowWithId(intentShow.id).subscribe({
                            runOnUiThread {
                                isInFavorites = true
                                fab.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
                            }
                        })
                    })
                    intentShow.id?.let { id ->
                        tmdb.getTVShowDetails(id).enqueue(object : Callback<TMDBTVShow> {
                            override fun onFailure(call: Call<TMDBTVShow>?, t: Throwable?) {

                            }

                            override fun onResponse(call: Call<TMDBTVShow>?, response: Response<TMDBTVShow>?) {
                                response?.body()?.let { responseTVShow ->
                                    bindTVShow(responseTVShow)
                                }
                            }
                        })

                    }


                }
            }
        }


        appbar.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
            title_bar.alpha = 1.0f - Math.abs(verticalOffset / appBarLayout.totalScrollRange.toFloat())
        })


    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        setUpPoster()
    }

    private fun setUpPoster() {
        Picasso.with(this).load(TMDB_CONST.POSTER_URL_THUMBNAIL + posterPath).placeholder(poster.drawable).fit().centerCrop().into(poster)
        Picasso.with(this).load(TMDB_CONST.POSTER_URL_LARGE + backdropPath).fit().centerCrop().into(backdrop)

    }

    private fun bindMovie(movie: TMDBMovie){

        //Poster
        posterPath = movie.posterPath.toString()
        backdropPath = movie.backdropPath.toString()

        //Header
        collapsing_toolbar.title = movie.title
        if(drawPosterOnCreate){
            setUpPoster()
        }
        poster.setOnClickListener { view ->
            viewMovieImages(view as ImageView, movie)
        }
        backdrop.setOnClickListener { view ->
            viewMovieImages(view as ImageView, movie)
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
        if(movie.credits?.cast != null && movie.credits!!.cast!!.isNotEmpty()){
            starring.visibility = View.VISIBLE
            starring_list.adapter = CastAdapter(movie.credits!!.cast!!.filterNotNull())
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

        //Trailer
        if(movie.videos?.results?.isNotEmpty() == true){
            trailer.setOnClickListener({
                val builderSingle = AlertDialog.Builder(DetailActivity@this)

                val arrayAdapter = ArrayAdapter<String>(DetailActivity@this, android.R.layout.select_dialog_item)
                val list = movie.videos!!.results!!.filterNotNull().filter { it.site == "YouTube" }

                list.forEach({
                    arrayAdapter.add(it.name)
                })

                builderSingle.setTitle(getString(R.string.watch_a_trailer))
                builderSingle.setNegativeButton("cancel", { dialog, _ ->
                    dialog.dismiss()
                })

                builderSingle.setAdapter(arrayAdapter, { _, which ->
                    val id = list[which].key
                    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
                    try {
                        DetailActivity@this.startActivity(appIntent)
                    } catch (e: Exception) {
                        DetailActivity@this.startActivity(webIntent)
                    }
                })

                builderSingle.show()
            })
            trailer.visibility = View.VISIBLE
        }else{
            trailer.visibility = View.GONE
        }

        fab.setOnClickListener({
            if(!isInFavorites){
                saveAsFavorite(movie)
            }else{
                removeFromFavorites(movie)
            }
        })

        network.visibility = View.GONE
        seasons.visibility = View.GONE
    }

    private fun bindTVShow(tvShow: TMDBTVShow) {
        //Poster
        posterPath = tvShow.posterPath.toString()
        backdropPath = tvShow.backdropPath.toString()

        //Header
        collapsing_toolbar.title = tvShow.name
        if(drawPosterOnCreate){
            setUpPoster()
        }
        poster.setOnClickListener { view ->
            viewTVShowImages(view as ImageView, tvShow)
        }
        backdrop.setOnClickListener { view ->
            viewTVShowImages(view as ImageView, tvShow)
        }


        val debutYear = tvShow.firstAirDate?.substring(0,4)
        val endYear = tvShow.lastAirDate?.substring(0,4)
        var info=""

        debutYear?.let{
            info+=debutYear
            if(tvShow.inProduction != true && endYear!=debutYear) {
                endYear?.let {
                    info += " - $endYear"
                }
            }
        }
        tvShow.numberOfSeasons?.let{
            if(!TextUtils.isEmpty(info)){
                info+=" · "
            }
            info+= String.format(resources.getQuantityString(R.plurals.seasons_nb,it), it)
        }
        tvShow.voteAverage?.let{
            if(!TextUtils.isEmpty(info)){
                info+=" · "
            }
            info+= "$it/10"
        }

        title_info_label.text = info


        //Creators
        var directedByString:String? = null
        tvShow.createdBy?.forEachIndexed { i, creator ->
            if(i==0){
                directedByString = getString(R.string.created_by) + " "
            }else{
                directedByString += ", "
            }

            directedByString+= creator?.name
        }
        if(directedByString==null){
            directed_by.visibility = View.GONE
        }else{
            directed_by.visibility = View.VISIBLE
            directed_by_label.text = directedByString
        }
        
        //Network
        var networksString:String? = null
        tvShow.networks?.forEachIndexed { i, network ->
            if(i==0){
                networksString = ""
            }else{
                networksString += ", "
            }

            networksString+= network?.name
        }
        if(networksString==null){
            network.visibility = View.GONE
        }else{
            network.visibility = View.VISIBLE
            network_label.text = networksString
        }

        //Cast
        if(tvShow.credits?.cast != null && tvShow.credits!!.cast!!.isNotEmpty()){
            starring.visibility = View.VISIBLE
            starring_list.adapter = CastAdapter(tvShow.credits!!.cast!!.filterNotNull())
        }else{
            starring.visibility = View.GONE
        }


        //Plot
        if(tvShow.overview.isNullOrEmpty()){
            plot.visibility = View.GONE
        }else{
            plot.visibility = View.VISIBLE
            plot_label.text = tvShow.overview
        }

        //Seasons
        if(tvShow.seasons != null && tvShow.numberOfSeasons!=null && tvShow.numberOfSeasons!! > 1){
            seasons.visibility = View.VISIBLE
            seasons_list.adapter = SeasonAdapter(tvShow.seasons!!.filterNotNull())
        }else{
            seasons.visibility = View.GONE
        }

        fab.setOnClickListener({
            if(!isInFavorites){
                saveAsFavorite(tvShow)
            }else{
                removeFromFavorites(tvShow)
            }
        })

        trailer.visibility = View.GONE
        country.visibility = View.GONE
        genre.visibility = View.GONE

    }

    private fun bindSeason(season : Season){
        //TODO
    }

    private fun removeFromFavorites(movie: TMDBMovie) {
        Schedulers.io().scheduleDirect({
            starringDB.favoritesDao().deleteFavoriteMovie(movie)
        })
        isInFavorites = false
        fab.setImageDrawable(getDrawable(R.drawable.ic_star_border_black_24dp))
    }

    private fun saveAsFavorite(movie: TMDBMovie) {
        Schedulers.io().scheduleDirect({
            starringDB.favoritesDao().insertFavoriteMovie(movie)
        })
        isInFavorites = true
        fab.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
    }

    private fun removeFromFavorites(tvShow: TMDBTVShow) {
        Schedulers.io().scheduleDirect({
            starringDB.favoritesDao().deleteFavoriteTVShow(tvShow)
        })
        isInFavorites = false
        fab.setImageDrawable(getDrawable(R.drawable.ic_star_border_black_24dp))
    }

    private fun saveAsFavorite(tvShow: TMDBTVShow) {
        Schedulers.io().scheduleDirect({
            starringDB.favoritesDao().insertFavoriteTVShow(tvShow)
        })
        isInFavorites = true
        fab.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
    }

    private fun viewMovieImages(view: ImageView, movie: TMDBMovie) {
        view.transitionName = ImagesActivity.EXTRA_IMAGE

        val extras = Bundle()

        val b: Bitmap = (view.drawable as BitmapDrawable).bitmap
        val bs = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, EXTRA_IMAGE).toBundle()

        val intent = Intent(DetailActivity@ this, ImagesActivity::class.java)
        extras.putByteArray(ImagesActivity.EXTRA_THUMBNAIL, bs.toByteArray())
        extras.putString(ImagesActivity.EXTRA_TITLE, movie.title)
        extras.putStringArray(ImagesActivity.EXTRA_URL, (if(view == backdrop) movie.images?.backdrops else movie.images?.posters)?.filterNotNull()?.map {
            TMDB_CONST.POSTER_URL_ORIGINAL + it.file_path
        }?.toTypedArray())
        intent.putExtras(extras)

        startActivity(intent,options)
    }


    private fun viewTVShowImages(view: ImageView, tvShow: TMDBTVShow) {
        view.transitionName = ImagesActivity.EXTRA_IMAGE

        val extras = Bundle()

        val b: Bitmap = (view.drawable as BitmapDrawable).bitmap
        val bs = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, EXTRA_IMAGE).toBundle()

        val intent = Intent(DetailActivity@ this, ImagesActivity::class.java)
        extras.putByteArray(ImagesActivity.EXTRA_THUMBNAIL, bs.toByteArray())
        extras.putString(ImagesActivity.EXTRA_TITLE, tvShow.name)
        extras.putStringArray(ImagesActivity.EXTRA_URL, (if(view == backdrop) tvShow.images?.backdrops else tvShow.images?.posters)?.filterNotNull()?.map {
            TMDB_CONST.POSTER_URL_ORIGINAL + it.file_path
        }?.toTypedArray())
        intent.putExtras(extras)

        startActivity(intent,options)
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_people_horizontal))

        override fun getItemCount(): Int = if(items.size >= 8) 8 else items.size

        inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(castItem: CastItem) {
                Picasso.with(itemView.context).load(TMDB_CONST.POSTER_URL_THUMBNAIL + castItem.profilePath).placeholder(R.color.material_grey_600).fit().centerInside().into(itemView.portrait)
                itemView.name_label.text = castItem.name

                this.itemView.setOnClickListener {
                    //TODO: implement detail activity UI & behaviour for cast
                }
            }

        }
    }

    inner class SeasonAdapter(private val items: List<Season>) : RecyclerView.Adapter<SeasonAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_seasons_horizontal))

        override fun getItemCount(): Int = items.size

        inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(season: Season) {
                Picasso.with(itemView.context).load(TMDB_CONST.POSTER_URL_THUMBNAIL + season.posterPath).placeholder(R.color.material_grey_600).fit().centerInside().into(itemView.portrait)
                itemView.name_label.text = season.name

                this.itemView.setOnClickListener {
                    //TODO: implement detail activity UI & behaviour for seasons
                }
            }

        }
    }
}
