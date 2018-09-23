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
import fr.flyingsquirrels.starring.model.CastItem
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.network.TMDB_CONST
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.adapter_media.view.*
import java.io.ByteArrayOutputStream

class CastAdapter(private val items: List<CastItem>) : RecyclerView.Adapter<CastAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_people_horizontal))

        override fun getItemCount(): Int = if(items.size >= 8) 8 else items.size

        inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(castItem: CastItem) {
                Picasso.get().load(TMDB_CONST.POSTER_URL_THUMBNAIL + castItem.profilePath).placeholder(R.color.material_grey_600).fit().centerInside().into(itemView.portrait)
                itemView.name_label.text = castItem.name

                itemView.setOnClickListener {

                    it.transitionName = DetailActivity.EXTRA_IMAGE
                    val intent = Intent(it.context, DetailActivity::class.java)
                    val options: Bundle? = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, it.portrait, DetailActivity.EXTRA_SHARED_PEOPLE).toBundle()
                    val extras = Bundle()

                    if(it.portrait.drawable != null && it.portrait.drawable is BitmapDrawable) {
                        val b: Bitmap = (it.portrait.drawable as BitmapDrawable).bitmap
                        val bs = ByteArrayOutputStream()
                        b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                        extras.putByteArray(DetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                    }

                    extras.putParcelable(DetailActivity.EXTRA_PAYLOAD,
                            Person(name = castItem.name,
                                    id = castItem.id,
                                    profilePath = castItem.profilePath))

                    extras.putString(DetailActivity.EXTRA_MEDIA_TYPE, DetailActivity.EXTRA_PERSON)

                    intent.putExtras(extras)

                    it.context.startActivity(intent, options)
                }
            }

        }
    }