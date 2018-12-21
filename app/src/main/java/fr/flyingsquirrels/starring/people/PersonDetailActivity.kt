package fr.flyingsquirrels.starring.people

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import fr.flyingsquirrels.starring.BaseDetailActivity
import fr.flyingsquirrels.starring.ImagesActivity
import fr.flyingsquirrels.starring.R
import fr.flyingsquirrels.starring.model.MediaCredit
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.network.TMDBCONST
import fr.flyingsquirrels.starring.people.view.MediaCreditAdapter
import fr.flyingsquirrels.starring.persons.viewmodel.PersonDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PersonDetailActivity : BaseDetailActivity<Person>() {


    private val vm : PersonDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val person: fr.flyingsquirrels.starring.model.Person? = intent.extras?.getParcelable(EXTRA_PAYLOAD)
        person?.let { intentPerson ->
            bindData(intentPerson)

            vm.getFavoritePersonWithId(intentPerson.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        isInFavorites = true
                        fab.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
                    }?.let{
                        disposables.add(it)
                    }
            intentPerson.id?.let { id ->
                vm.getPersonDetails(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers
                                .mainThread())
                        .subscribe(::bindData, ::handleError)?.let{
                            disposables.add(it)
                        }
            }


        }
    }

    override fun bindData(data : Person) {

        plot.visibility = View.GONE
        genre.visibility = View.GONE
        episodes.visibility = View.GONE
        starring.visibility = View.GONE
        directed_by.visibility = View.GONE
        seasons.visibility = View.GONE
        network.visibility = View.GONE
        country.visibility = View.GONE
        genre.visibility = View.GONE
        trailer.visibility = View.GONE

        //Poster
        posterPath = data.profilePath.toString()
        backdropPath = data.profilePath.toString()

        poster.setOnClickListener { view ->
            viewImages(view as ImageView, data)
        }
        backdrop.setOnClickListener { view ->
            viewImages(view as ImageView, data)
        }

        //Header
        collapsing_toolbar.title = data.name
        if(drawPosterOnCreate){
            setUpPoster()
        }

        var info=""

        data.birthday?.let{ birthday ->
            if(birthday.length>=4){
                val year = birthday.substring(0,4)

                year.let{ birthyear ->
                    data.deathday?.let{ deathday ->
                        if(deathday.length >=4){
                            val deathyear = deathday.substring(0,4)

                            info+= "$birthyear - $deathyear"
                        }
                    } ?: let{ _ ->

                        val dob = Calendar.getInstance()
                        val today = Calendar.getInstance()

                        dob.time = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(birthday)

                        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

                        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                            age--
                        }

                        info+= "$birthyear (${getString(R.string.age,age)})"
                    }
                }
            }
        }

        title_info_label.text = info

        //Bio
        if(!data.biography.isNullOrEmpty()){
            bio.visibility = View.VISIBLE
            bio_label.text = data.biography
        } else {
            bio.visibility = View.GONE
        }

        //Known for
        known_for.visibility = View.GONE
        data.mediaCredits?.let{ mediaCredits ->
            val cast = arrayListOf<MediaCredit?>()
            val crew = arrayListOf<MediaCredit?>()

            cast.addAll(mediaCredits.cast ?: arrayListOf())
            crew.addAll(mediaCredits.crew ?: arrayListOf())

            val knownForList = cast.union(crew.asIterable()).asSequence().take(8).filterNotNull().toList()

            if(knownForList.isNotEmpty()) {
                known_for.visibility = View.VISIBLE
                known_for_list.adapter = MediaCreditAdapter(knownForList)
            }
        }

        fab.setOnClickListener {
            if(!isInFavorites){
                saveAsFavorite(data)
            }else{
                removeFromFavorites(data)
            }
        }

        Handler().post{ scroll.scrollTo(0,0) }

    }

    override fun removeFromFavorites(data: Person) {
        super.removeFromFavorites(data)
        vm.deleteFavoritePerson(data).subscribeOn(Schedulers.io()).subscribe()?.let{
            disposables.add(it)
        }
    }

    override fun saveAsFavorite(data: Person) {
        super.saveAsFavorite(data)
        vm.insertFavoritePerson(data).subscribeOn(Schedulers.io()).subscribe()?.let{
            disposables.add(it)
        }
    }


    override fun viewImages(view: ImageView, data: Person) {
        super.viewImages(view, data)

        val extras = Bundle()

        val b: Bitmap? = (view.drawable as BitmapDrawable?)?.bitmap
        b?.let { bitmap ->

            val bs = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, EXTRA_IMAGE).toBundle()

            val intent = Intent(this, ImagesActivity::class.java)
            extras.putByteArray(ImagesActivity.EXTRA_THUMBNAIL, bs.toByteArray())
            extras.putString(ImagesActivity.EXTRA_TITLE, data.name)
            extras.putStringArray(ImagesActivity.EXTRA_URL, (data.images?.profiles)?.asSequence()?.filterNotNull()?.map {
                TMDBCONST.POSTER_URL_ORIGINAL + it.file_path
            }?.toList()?.toTypedArray())
            intent.putExtras(extras)

            startActivity(intent, options)
        }
    }
}