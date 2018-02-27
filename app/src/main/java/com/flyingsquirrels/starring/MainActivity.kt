package com.flyingsquirrels.starring

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import dagger.Component
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_movies.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var tmdb: TMDBRetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as StarringApp).network.inject(this)

        list.layoutManager = GridLayoutManager(this, 2)

        tmdb.getTopRated().enqueue(object : Callback<TMDBMovieResponse> {
            override fun onFailure(call: Call<TMDBMovieResponse>?, t: Throwable?) {
                Log.e("","error",t)
            }

            override fun onResponse(call: Call<TMDBMovieResponse>?, response: Response<TMDBMovieResponse>?) {
                if (response != null) {
                    val tmdbResponse = response.body()!!
                    list.adapter = FilmsAdapter(tmdbResponse.results)
                }
            }
        })
    }

    class FilmsAdapter(private val items: List<TMDBMovie>) : RecyclerView.Adapter<FilmsAdapter.Holder>() {
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(R.layout.adapter_movies))

        override fun getItemCount(): Int = items.size

        class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(movie: TMDBMovie) {
                Picasso.with(itemView.context).load("https://image.tmdb.org/t/p/w300"+movie.posterPath).placeholder(R.color.material_grey_600).fit().centerCrop().into(itemView.cover)
            }

        }

    }
}

private fun ViewGroup.inflate(adapter_layout: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(adapter_layout,this, attachToRoot)
}


data class TMDBMovieResponse(@field:SerializedName("results")  var results: List<TMDBMovie>)
data class TMDBMovie(@field:SerializedName("poster_path")  var posterPath: String,
                     @field:SerializedName("adult") var isAdult: Boolean,
                     @field:SerializedName("overview") var overview: String,
                     @field:SerializedName("release_date") var releaseDate: String,
                     @field:SerializedName("genre_ids") var genreIds: List<Int>,
                     @field:SerializedName("id") var id: Int,
                     @field:SerializedName("original_title") var originalTitle: String,
                     @field:SerializedName("original_language") var originalLanguage: String,
                     @field:SerializedName("title") var title: String,
                     @field:SerializedName("backdrop_path") var backdropPath: String,
                     @field:SerializedName("popularity") var popularity: Double,
                     @field:SerializedName("vote_count") var voteCount: Int?,
                     @field:SerializedName("video") var video: Boolean?,
                     @field:SerializedName("vote_average") var voteAverage: Double)


@Singleton
@Component(modules = [(NetworkModule::class)])
interface NetworkComponent {
    fun inject(activity: MainActivity)
}