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
import fr.flyingsquirrels.starring.MainActivity
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.TVShow
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.tvshows.TVShowDetailActivity
import fr.flyingsquirrels.starring.utils.AndroidPair
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_movies.view.*
import java.io.ByteArrayOutputStream

class TVShowAdapter(var items: List<TVShow>) : RecyclerView.Adapter<TVShowAdapter.TVShowHolder>() {
    override fun onBindViewHolder(holder: TVShowHolder, position: Int) = holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowHolder = TVShowHolder(parent.inflate(R.layout.adapter_movies)!!)

    override fun getItemCount(): Int = items.size

    class TVShowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(tvShow : TVShow){
            Picasso.get().load(TMDBCONST.POSTER_URL_THUMBNAIL + tvShow.posterPath).placeholder(R.color.grey600).fit().centerCrop().into(itemView.cover)

            this.itemView.setOnClickListener { view ->

                view.transitionName = BaseDetailActivity.EXTRA_IMAGE

                val intent = Intent(view.context, TVShowDetailActivity::class.java)

                var options: Bundle? = null
                val extras = Bundle()

                if (view.context != null && view.context is MainActivity) {
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation(view.context as Activity,
                            AndroidPair.create<View, String>(view, BaseDetailActivity.EXTRA_SHARED_POSTER),
                            AndroidPair.create<View, String>((view.context as MainActivity).nav, BaseDetailActivity.EXTRA_SHARED_NAV)
                    ).toBundle()
                    extras.putInt(BaseDetailActivity.EXTRA_NAV_ITEM, (view.context as MainActivity).nav.selectedItemId)
                }
                if(view.cover.drawable != null && view.cover.drawable is BitmapDrawable) {
                    val b: Bitmap = (itemView.cover.drawable as BitmapDrawable).bitmap
                    val bs = ByteArrayOutputStream()
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bs)

                    extras.putByteArray(BaseDetailActivity.EXTRA_THUMBNAIL, bs.toByteArray())
                }
                extras.putParcelable(BaseDetailActivity.EXTRA_PAYLOAD, tvShow)

                intent.putExtras(extras)
                view.context.startActivity(intent, options)
            }
        }

    }
}