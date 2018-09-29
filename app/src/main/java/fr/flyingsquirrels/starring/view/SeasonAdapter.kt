package fr.flyingsquirrels.starring.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import fr.flyingsquirrels.starring.DetailActivity
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.Season
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.adapter_media.view.*
import java.io.ByteArrayOutputStream

class SeasonAdapter(private val items: List<Season>) : RecyclerView.Adapter<SeasonAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_media_horizontal))

        override fun getItemCount(): Int = items.size

        inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(season: Season) {
                Picasso.get().load(TMDBCONST.POSTER_URL_THUMBNAIL + season.posterPath).placeholder(R.color.material_grey_600).fit().centerInside().into(itemView.portrait)
                itemView.name_label.text = season.name

                itemView.setOnClickListener {

                    it.transitionName = DetailActivity.EXTRA_IMAGE
                    val intent = Intent(it.context, DetailActivity::class.java)
                    val options: Bundle? = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, it.portrait, DetailActivity.EXTRA_SHARED_SEASON).toBundle()
                    val extras = Bundle()

                    if(it.portrait.drawable != null && it.portrait.drawable is BitmapDrawable) {
                        val b: Bitmap = (it.portrait.drawable as BitmapDrawable).bitmap
                        val bs = ByteArrayOutputStream()
                        b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                        extras.putByteArray(DetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                    }
                    extras.putString(DetailActivity.EXTRA_MEDIA_TYPE, DetailActivity.EXTRA_TV_SHOW_SEASON)
                    extras.putParcelable(DetailActivity.EXTRA_PAYLOAD, season)

                    intent.putExtras(extras)

                    it.context.startActivity(intent, options)
                }
            }

        }
    }