package fr.flyingsquirrels.starring.tvshows

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import com.uber.autodispose.autoDisposable
import fr.flyingsquirrels.starring.BaseDetailActivity
import fr.flyingsquirrels.starring.ImagesActivity
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.TVShow
import fr.flyingsquirrels.starring.movies.view.CastAdapter
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.tvshows.view.SeasonAdapter
import fr.flyingsquirrels.starring.tvshows.viewmodel.TVShowDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class TVShowDetailActivity : BaseDetailActivity<TVShow>() {

    companion object {
        const val EXTRA_TV_SHOW = "tv_show"
    }


    private val vm : TVShowDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tvShow: TVShow? = intent.extras?.getParcelable(EXTRA_PAYLOAD)

        tvShow?.let { intentShow ->
            bindData(intentShow)

            vm.getFavoriteTVShowWithId(intentShow.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).autoDisposable(scopeProvider).subscribe {
                isInFavorites = true
                fab.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
            }
            intentShow.id?.let { id ->
                vm.getTVShowDetails(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).autoDisposable(scopeProvider).subscribe(this@TVShowDetailActivity::bindData)
            }


        }
    }

    override fun bindData(data: TVShow) {

        known_for.visibility = View.GONE
        bio.visibility = View.GONE
        trailer.visibility = View.GONE
        country.visibility = View.GONE
        genre.visibility = View.GONE
        episodes.visibility = View.GONE

        //Poster
        posterPath = data.posterPath.toString()
        backdropPath = data.backdropPath.toString()

        //Header
        collapsing_toolbar.title = data.name
        if(drawPosterOnCreate){
            setUpPoster()
        }
        poster.setOnClickListener { view ->
            viewImages(view as ImageView, data)
        }
        backdrop.setOnClickListener { view ->
            viewImages(view as ImageView, data)
        }


        val debutYear = data.firstAirDate?.substring(0,4)
        val endYear = data.lastAirDate?.substring(0,4)
        var info=""

        debutYear?.let{ _ ->
            info+=debutYear
            if(data.inProduction != true && endYear!=debutYear) {
                endYear?.let {
                    info += " - $endYear"
                }
            }
        }
        data.numberOfSeasons?.let{
            if(!TextUtils.isEmpty(info)){
                info+=" · "
            }
            info+= String.format(resources.getQuantityString(R.plurals.seasons_nb,it), it)
        }
        data.voteAverage?.let{
            if(!TextUtils.isEmpty(info)){
                info+=" · "
            }
            info+= "$it/10"
        }

        title_info_label.text = info


        //Creators
        var createdByString:String? = null
        data.createdBy?.forEachIndexed { i, creator ->
            if(i==0){
                createdByString = getString(R.string.created_by) + " "
            }else{
                createdByString += ", "
            }

            createdByString+= creator?.name
        }
        if(createdByString==null){
            directed_by.visibility = View.GONE
        }else{
            directed_by.visibility = View.VISIBLE
            directed_by_label.text = createdByString
        }

        //Network
        var networksString:String? = null
        data.networks?.forEachIndexed { i, network ->
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

        //Seasons
        if(data.seasons != null && data.seasons!!.isNotEmpty()){
            seasons.visibility = View.VISIBLE
            seasons_list.adapter = SeasonAdapter(data.seasons!!.asSequence().filterNotNull().map {
                it.apply {
                    tvId = data.id
                    backdropPath = data.backdropPath
                }
            }.toList())
        }else{
            seasons.visibility = View.GONE
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


    override fun removeFromFavorites(data: TVShow) {
        super.removeFromFavorites(data)

        vm.deleteFavoriteTVShow(data).subscribeOn(Schedulers.io()).autoDisposable(scopeProvider).subscribe()
    }

    override fun saveAsFavorite(data: TVShow) {
        super.saveAsFavorite(data)

        vm.insertFavoriteTVShow(data).subscribeOn(Schedulers.io()).autoDisposable(scopeProvider).subscribe()
    }

    override fun viewImages(view: ImageView, data: TVShow) {
        super.viewImages(view, data)

        val extras = Bundle()

        val b: Bitmap? = (view.drawable as BitmapDrawable?)?.bitmap
        b?.let { bitmap ->
            val bs = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, EXTRA_IMAGE).toBundle()

            val intent = Intent(this, ImagesActivity::class.java)
            extras.putByteArray(ImagesActivity.EXTRA_THUMBNAIL, bs.toByteArray())
            extras.putString(ImagesActivity.EXTRA_TITLE, data.name)
            extras.putStringArray(ImagesActivity.EXTRA_URL, (if (view == backdrop) data.images?.backdrops else data.images?.posters)?.asSequence()?.filterNotNull()?.map {
                TMDBCONST.POSTER_URL_ORIGINAL + it.file_path
            }?.toList()?.toTypedArray())
            intent.putExtras(extras)

            startActivity(intent, options)
        }
    }
}