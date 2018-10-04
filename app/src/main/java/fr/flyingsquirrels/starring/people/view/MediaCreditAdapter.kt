package fr.flyingsquirrels.starring.people.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.flyingsquirrels.starring.BaseDetailActivity
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.MediaCredit
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.model.TVShow
import fr.flyingsquirrels.starring.movies.MovieDetailActivity
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.tvshows.TVShowDetailActivity
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_media.view.*
import java.io.ByteArrayOutputStream

class MediaCreditAdapter(private val items: List<MediaCredit>) : RecyclerView.Adapter<MediaCreditAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_media_horizontal)!!)

    override fun getItemCount(): Int = items.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mediaCredit: MediaCredit) {
            Picasso.get().load(TMDBCONST.POSTER_URL_THUMBNAIL + mediaCredit.posterPath).placeholder(R.color.material_grey_600).fit().centerInside().into(itemView.portrait)
            itemView.name_label.text = when(mediaCredit.mediaType) {
                MOVIE -> mediaCredit.title
                TV -> mediaCredit.name
                else -> ""
            }

            itemView.setOnClickListener { view ->

                var intent: Intent? = null

                view.transitionName = BaseDetailActivity.EXTRA_IMAGE
                val options: Bundle? = ActivityOptionsCompat.makeSceneTransitionAnimation(view.context as Activity, view.portrait, BaseDetailActivity.EXTRA_SHARED_POSTER).toBundle()
                val extras = Bundle()

                if(view.portrait.drawable != null && view.portrait.drawable is BitmapDrawable) {
                    val b: Bitmap = (view.portrait.drawable as BitmapDrawable).bitmap
                    val bs = ByteArrayOutputStream()
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                    extras.putByteArray(BaseDetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                }
                if(view.context is BaseDetailActivity<*>) {
                    extras.putInt(BaseDetailActivity.EXTRA_NAV_ITEM, (view.context as BaseDetailActivity<*>).nav.selectedItemId)
                }


                when(mediaCredit.mediaType){
                    MOVIE -> {
                        intent = Intent(view.context, MovieDetailActivity::class.java)
                        extras.putParcelable(BaseDetailActivity.EXTRA_PAYLOAD,
                                Movie(title = mediaCredit.title,
                                        id = mediaCredit.id,
                                        posterPath = mediaCredit.posterPath,
                                        overview = mediaCredit.overview,
                                        backdropPath = mediaCredit.backdropPath))
                    }
                    TV -> {
                        intent = Intent(view.context, TVShowDetailActivity::class.java)
                        extras.putParcelable(BaseDetailActivity.EXTRA_PAYLOAD,
                                TVShow(name = mediaCredit.name,
                                        id = mediaCredit.id,
                                        posterPath = mediaCredit.posterPath,
                                        overview = mediaCredit.overview,
                                        backdropPath = mediaCredit.backdropPath))
                    }
                }

                intent?.let{
                    it.putExtras(extras)
                    view.context.startActivity(it, options)
                }
            }
        }
    }


    companion object {
        private const val MOVIE = "movie"
        private const val TV = "tv"
    }
}