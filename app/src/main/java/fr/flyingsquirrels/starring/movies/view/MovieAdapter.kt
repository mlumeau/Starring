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
import fr.flyingsquirrels.starring.MainActivity
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.movies.MovieDetailActivity
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.utils.AndroidPair
import fr.flyingsquirrels.starring.utils.inflate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_movies.view.*
import java.io.ByteArrayOutputStream

class MovieAdapter(var items: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder = MovieHolder(parent.inflate(R.layout.adapter_movies)!!)

    override fun getItemCount(): Int = items.size


    inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie : Movie){
            Picasso.get().load(TMDBCONST.POSTER_URL_THUMBNAIL+ movie.posterPath).placeholder(R.color.grey600).fit().centerCrop().into(itemView.cover)

            this.itemView.setOnClickListener { view ->

                view.transitionName = BaseDetailActivity.EXTRA_IMAGE

                val intent = Intent(view.context, MovieDetailActivity::class.java)

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
                extras.putParcelable(BaseDetailActivity.EXTRA_PAYLOAD, movie)

                intent.putExtras(extras)
                view.context.startActivity(intent, options)
            }
        }

    }
}