package fr.flyingsquirrels.starring.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.CastItem
import fr.flyingsquirrels.starring.network.TMDB_CONST
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.adapter_people.view.*

class CastAdapter(private val items: List<CastItem>) : RecyclerView.Adapter<CastAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_people_horizontal))

        override fun getItemCount(): Int = if(items.size >= 8) 8 else items.size

        inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(castItem: CastItem) {
                Picasso.with(itemView.context).load(TMDB_CONST.POSTER_URL_THUMBNAIL + castItem.profilePath).placeholder(R.color.material_grey_600).fit().centerInside().into(itemView.portrait)
                itemView.name_label.text = castItem.name

                itemView.setOnClickListener {
                    //TODO: implement detail activity UI & behaviour for cast
                }
            }

        }
    }