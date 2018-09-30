package fr.flyingsquirrels.starring.view

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
import fr.flyingsquirrels.starring.DetailActivity
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.MediaCredit
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.model.TVShow
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.utils.inflate
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

            itemView.setOnClickListener {

                it.transitionName = DetailActivity.EXTRA_IMAGE
                val intent = Intent(it.context, DetailActivity::class.java)
                val options: Bundle? = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, it.portrait, DetailActivity.EXTRA_SHARED_POSTER).toBundle()
                val extras = Bundle()

                if(it.portrait.drawable != null && it.portrait.drawable is BitmapDrawable) {
                    val b: Bitmap = (it.portrait.drawable as BitmapDrawable).bitmap
                    val bs = ByteArrayOutputStream()
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                    extras.putByteArray(DetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                }
                val type: String
                when(mediaCredit.mediaType){
                    MOVIE -> {
                        type = DetailActivity.EXTRA_MOVIE
                        extras.putParcelable(DetailActivity.EXTRA_PAYLOAD,
                                Movie(title = mediaCredit.title,
                                        id = mediaCredit.id,
                                        posterPath = mediaCredit.posterPath,
                                        overview = mediaCredit.overview,
                                        backdropPath = mediaCredit.backdropPath))
                    }
                    TV -> {
                        type = DetailActivity.EXTRA_TV_SHOW
                        extras.putParcelable(DetailActivity.EXTRA_PAYLOAD,
                                TVShow(name = mediaCredit.name,
                                        id = mediaCredit.id,
                                        posterPath = mediaCredit.posterPath,
                                        overview = mediaCredit.overview,
                                        backdropPath = mediaCredit.backdropPath))
                    }
                    else -> type = ""
                }

                extras.putString(DetailActivity.EXTRA_MEDIA_TYPE, type)

                intent.putExtras(extras)

                it.context.startActivity(intent, options)
            }
        }
    }


    companion object {
        private const val MOVIE = "movie"
        private const val TV = "tv"
    }
}