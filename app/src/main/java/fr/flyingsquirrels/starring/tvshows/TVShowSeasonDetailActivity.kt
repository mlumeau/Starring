package fr.flyingsquirrels.starring.tvshows

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import fr.flyingsquirrels.starring.BaseDetailActivity
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.Season
import fr.flyingsquirrels.starring.tvshows.view.EpisodeAdapter
import fr.flyingsquirrels.starring.tvshows.viewmodel.TVShowSeasonDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.ext.android.inject

class TVShowSeasonDetailActivity : BaseDetailActivity<Season>() {

    private val vm: TVShowSeasonDetailViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tvSeason: Season? = intent.extras?.getParcelable(EXTRA_PAYLOAD)
        tvSeason?.let { intentSeason ->
            bindData(intentSeason)
            if(intentSeason.tvId != null && intentSeason.seasonNumber != null) {

                vm.getTVShowSeasonDetails(intentSeason.tvId!!, intentSeason.seasonNumber!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe{ responseTVShowSeason ->
                            responseTVShowSeason.apply {
                                tvId = intentSeason.id
                                backdropPath = intentSeason.backdropPath
                            }
                            bindData(responseTVShowSeason)
                        }?.let{
                            disposables.add(it)
                        }

            }
        }
    }

    override fun bindData(data : Season){

        known_for.visibility = View.GONE
        bio.visibility = View.GONE
        fab.systemUiVisibility = View.GONE
        starring.visibility = View.GONE
        directed_by.visibility = View.GONE
        seasons.visibility = View.GONE
        network.visibility = View.GONE
        country.visibility = View.GONE
        genre.visibility = View.GONE

        //Poster
        posterPath = data.posterPath.toString()
        backdropPath = data.backdropPath.toString()

        //Header
        collapsing_toolbar.title = data.name
        if(drawPosterOnCreate){
            setUpPoster()
        }

        var info=""

        data.airDate?.let{ airDate ->
            if(airDate.length>=4){
                val year = data.airDate?.substring(0,4)

                year?.let{
                    info+=year
                }
            }
        }

        data.episodeCount?.let{
            if(!TextUtils.isEmpty(info)){
                info+=" · "
            }
            info+= String.format(resources.getQuantityString(R.plurals.episodes_nb,it), it)
        }

        title_info_label.text = info

        //Plot
        if(data.overview.isNullOrEmpty()){
            plot.visibility = View.GONE
        }else{
            plot.visibility = View.VISIBLE
            plot_label.text = data.overview
        }

        //Episodes
        if(data.episodes != null && data.episodes!!.isNotEmpty()){
            episodes.visibility = View.VISIBLE
            episodes_list.adapter = EpisodeAdapter(data.episodes!!.filterNotNull())
        }else{
            episodes.visibility = View.GONE
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

        Handler().post{ scroll.scrollTo(0,0) }
    }
}