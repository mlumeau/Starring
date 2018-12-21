package fr.flyingsquirrels.starring.movies.view

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
import fr.flyingsquirrels.starring.model.CastItem
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.people.PersonDetailActivity
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_people.view.*
import java.io.ByteArrayOutputStream

class CastAdapter(private val items: List<CastItem>) : RecyclerView.Adapter<CastAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_people_horizontal)!!)

        override fun getItemCount(): Int = if(items.size >= 8) 8 else items.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(castItem: CastItem) {
                Picasso.get().load(TMDBCONST.POSTER_URL_THUMBNAIL + castItem.profilePath).placeholder(R.color.material_grey_600).fit().centerInside().into(itemView.portrait)
                itemView.name_label.text = castItem.name

                itemView.setOnClickListener {

                    it.transitionName = BaseDetailActivity.EXTRA_IMAGE
                    val intent = Intent(it.context, PersonDetailActivity::class.java)
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

                    extras.putParcelable(BaseDetailActivity.EXTRA_PAYLOAD,
                            Person(name = castItem.name,
                                    id = castItem.id,
                                    profilePath = castItem.profilePath))

                    intent.putExtras(extras)

                    it.context.startActivity(intent, options)
                }
            }

        }
    }