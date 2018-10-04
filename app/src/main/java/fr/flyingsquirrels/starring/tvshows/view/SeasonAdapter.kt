package fr.flyingsquirrels.starring.tvshows.view

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
import fr.flyingsquirrels.starring.model.Season
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.tvshows.TVShowSeasonDetailActivity
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_media.view.*
import java.io.ByteArrayOutputStream

class SeasonAdapter(private val items: List<Season>) : RecyclerView.Adapter<SeasonAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_media_horizontal)!!)

        override fun getItemCount(): Int = items.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(season: Season) {
                Picasso.get().load(TMDBCONST.POSTER_URL_THUMBNAIL + season.posterPath).placeholder(R.color.material_grey_600).fit().centerInside().into(itemView.portrait)
                itemView.name_label.text = season.name

                itemView.setOnClickListener {

                    it.transitionName = BaseDetailActivity.EXTRA_IMAGE
                    val intent = Intent(it.context, TVShowSeasonDetailActivity::class.java)
                    val options: Bundle? = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, it.portrait, BaseDetailActivity.EXTRA_SHARED_POSTER).toBundle()
                    val extras = Bundle()

                    if(it.portrait.drawable != null && it.portrait.drawable is BitmapDrawable) {
                        val b: Bitmap = (it.portrait.drawable as BitmapDrawable).bitmap
                        val bs = ByteArrayOutputStream()
                        b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                        extras.putByteArray(BaseDetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                    }

                    if(it.context is BaseDetailActivity<*>) {
                        extras.putInt(BaseDetailActivity.EXTRA_NAV_ITEM, (it.context as BaseDetailActivity<*>).nav.selectedItemId)
                    }

                    extras.putParcelable(BaseDetailActivity.EXTRA_PAYLOAD, season)

                    intent.putExtras(extras)

                    it.context.startActivity(intent, options)
                }
            }

        }
    }