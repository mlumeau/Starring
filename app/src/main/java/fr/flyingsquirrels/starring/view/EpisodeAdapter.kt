package fr.flyingsquirrels.starring.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.Episode
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.adapter_episodes.view.*

class EpisodeAdapter(private val items: List<Episode>) : RecyclerView.Adapter<EpisodeAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_episodes))

        override fun getItemCount(): Int = items.size

        inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(episode: Episode) {

                itemView.episode_name.text = episode.name
                itemView.episode_number.text = episode.episodeNumber.toString().takeLast(3)

                if(episode.overview.isNullOrEmpty()){
                    itemView.episode_overview.visibility = View.GONE
                }else{
                    itemView.episode_overview.visibility = View.VISIBLE
                    itemView.episode_overview.text = episode.overview
                }

                if(episode.airDate.isNullOrEmpty()){
                    itemView.episode_airing_date.visibility = View.GONE
                }else{
                    itemView.episode_airing_date.visibility = View.VISIBLE
                    itemView.episode_airing_date.text = episode.airDate
                }

                if(episode.voteAverage == null){
                    itemView.episode_rating.visibility = View.GONE
                }else{
                    itemView.episode_rating.visibility = View.VISIBLE
                    itemView.episode_rating.text = "${Math.round(episode.voteAverage!!*10)/10f}/10"
                }


                var guestsString:String? = null
                episode.guestStars?.filterNotNull()?.take(3)?.forEachIndexed { i, guest ->
                    if(i==0){
                        guestsString = itemView.context.getString(R.string.guests) + " "
                    }else{
                        guestsString += ", "
                    }

                    guestsString+= guest.name
                }
                if(guestsString==null){
                    itemView.episode_guests.visibility = View.GONE
                }else{
                    itemView.episode_guests.visibility = View.VISIBLE
                    itemView.episode_guests.text = guestsString
                }

                itemView.expand.setOnClickListener {
                    itemView.expandable_layout.toggle()
                }
            }

        }
    }