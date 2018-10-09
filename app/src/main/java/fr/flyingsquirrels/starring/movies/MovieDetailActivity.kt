package fr.flyingsquirrels.starring.movies

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import fr.flyingsquirrels.starring.BaseDetailActivity
import fr.flyingsquirrels.starring.ImagesActivity
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.movies.view.CastAdapter
import fr.flyingsquirrels.starring.movies.viewmodel.MovieDetailViewModel
import fr.flyingsquirrels.starring.network.TMDBCONST
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class MovieDetailActivity : BaseDetailActivity<Movie>() {

    private val vm : MovieDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movie: Movie? = intent.extras?.getParcelable(EXTRA_PAYLOAD)
        movie?.let { intentMovie ->
            bindData(intentMovie)

            vm.getFavoriteMovieWithId(intentMovie.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        isInFavorites = true
                        fab.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
                    }?.let{
                        disposables.add(it)
                    }
            intentMovie.id?.let { id ->
                vm.getMovieDetails(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this@MovieDetailActivity::bindData)?.let{
                            disposables.add(it)
                        }
            }
        }
    }

    override fun bindData(data: Movie) {

        known_for.visibility = View.GONE
        bio.visibility = View.GONE
        network.visibility = View.GONE
        seasons.visibility = View.GONE
        episodes.visibility = View.GONE

        //Poster
        posterPath = data.posterPath.toString()
        backdropPath = data.backdropPath.toString()

        //Header
        collapsing_toolbar.title = data.title
        if(drawPosterOnCreate){
            setUpPoster()
        }
        poster.setOnClickListener { view ->
            viewImages(view as ImageView, data)
        }
        backdrop.setOnClickListener { view ->
            viewImages(view as ImageView, data)
        }


        val year = data.releaseDate?.substring(0,4)
        var info=""

        year?.let{
            info+=year
        }
        data.runtime?.let{
            if(!TextUtils.isEmpty(info)){
                info+=" · "
            }
            info+= "$it min"
        }
        data.voteAverage?.let{
            if(!TextUtils.isEmpty(info)){
                info+=" · "
            }
            info+= "$it/10"
        }

        title_info_label.text = info


        //Directors
        var directedByString:String? = null
        data.getDirectors()?.forEachIndexed { i, director ->
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
        data.productionCountries?.forEachIndexed { i, country ->
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
        data.genres?.forEachIndexed { i, genre ->
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
        if(data.credits?.cast != null && data.credits!!.cast!!.isNotEmpty()){
            starring.visibility = View.VISIBLE
            starring_list.adapter = CastAdapter(data.credits!!.cast!!.filterNotNull())
        }else{
            starring.visibility = View.GONE
        }

        //Plot
        if(data.overview.isNullOrEmpty()){
            plot.visibility = View.GONE
        }else{
            plot.visibility = View.VISIBLE
            plot_label.text = data.overview
        }

        //Trailer
        if(data.videos?.results?.isNotEmpty() == true){
            trailer.setOnClickListener { _ ->
                val builderSingle = AlertDialog.Builder(this)

                val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
                val list = data.videos!!.results!!.asSequence().filterNotNull().filter { it.site == "YouTube" }.toList()

                list.forEach {
                    arrayAdapter.add(it.name)
                }

                builderSingle.setTitle(getString(R.string.watch_a_trailer))
                builderSingle.setNegativeButton("cancel") { dialog, _ ->
                    dialog.dismiss()
                }

                builderSingle.setAdapter(arrayAdapter) { _, which ->
                    val id = list[which].key
                    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
                    try {
                        startActivity(appIntent)
                    } catch (e: Exception) {
                        startActivity(webIntent)
                    }
                }

                builderSingle.show()
            }
            trailer.visibility = View.VISIBLE
        }else{
            trailer.visibility = View.GONE
        }

        fab.setOnClickListener {
            if(!isInFavorites){
                saveAsFavorite(data)
            }else{
                removeFromFavorites(data)
            }
        }

        Handler().post{ scroll.scrollTo(0,0) }

    }

    override fun removeFromFavorites(data: Movie) {
        super.removeFromFavorites(data)
        vm.deleteFavoriteMovie(data).subscribeOn(Schedulers.io()).subscribe()?.let{
            disposables.add(it)
        }
    }

    override fun saveAsFavorite(data: Movie) {
        super.saveAsFavorite(data)
        vm.insertFavoriteMovie(data).subscribeOn(Schedulers.io()).subscribe()?.let{
            disposables.add(it)
        }
    }

    override fun viewImages(view: ImageView, data: Movie) {
        super.viewImages(view, data)

        val extras = Bundle()

        val b: Bitmap? = (view.drawable as BitmapDrawable?)?.bitmap
        b?.let{ bitmap ->
            val bs = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, EXTRA_IMAGE).toBundle()

            val intent = Intent(this, ImagesActivity::class.java)
            extras.putByteArray(ImagesActivity.EXTRA_THUMBNAIL, bs.toByteArray())
            extras.putString(ImagesActivity.EXTRA_TITLE, data.title)
            extras.putStringArray(ImagesActivity.EXTRA_URL, (if(view == backdrop) data.images?.backdrops else data.images?.posters)?.asSequence()?.filterNotNull()?.map {
                TMDBCONST.POSTER_URL_ORIGINAL + it.file_path
            }?.toList()?.toTypedArray())
            intent.putExtras(extras)

            startActivity(intent,options)
        }
    }
}