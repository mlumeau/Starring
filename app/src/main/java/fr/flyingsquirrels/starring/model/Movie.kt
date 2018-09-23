package fr.flyingsquirrels.starring.model


import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Entity
data class Movie(

        @field:SerializedName("original_language")
        var originalLanguage: String? = null,

        @field:SerializedName("imdb_id")
        var imdbId: String? = null,

        @field:SerializedName("videos")
        var videos: VideoResponse? = null,

        @field:SerializedName("video")
        var video: Boolean? = null,

        @field:SerializedName("title")
        var title: String? = null,

        @field:SerializedName("backdrop_path")
        var backdropPath: String? = null,

        @field:SerializedName("revenue")
        var revenue: Int? = null,

        @field:SerializedName("credits")
        var credits: PeopleCredits? = null,

        @field:SerializedName("genres")
        var genres: List<Genre?>? = null,

        @field:SerializedName("popularity")
        var popularity: Double? = null,

        @field:SerializedName("production_countries")
        var productionCountries: List<ProductionCountry?>? = null,

        @PrimaryKey
        @field:SerializedName("id")
        var id: Int? = null,

        @field:SerializedName("vote_count")
        var voteCount: Int? = null,

        @field:SerializedName("budget")
        var budget: Int? = null,

        @field:SerializedName("overview")
        var overview: String? = null,

        @field:SerializedName("original_title")
        var originalTitle: String? = null,

        @field:SerializedName("runtime")
        var runtime: Int? = null,

        @field:SerializedName("poster_path")
        var posterPath: String? = null,

        @field:SerializedName("spoken_languages")
        var spokenLanguages: List<SpokenLanguage?>? = null,

        @field:SerializedName("production_companies")
        var productionCompanies: List<ProductionCompany?>? = null,

        @field:SerializedName("release_date")
        var releaseDate: String? = null,

        @field:SerializedName("vote_average")
        var voteAverage: Double? = null,

        @field:SerializedName("tagline")
        var tagline: String? = null,

        @field:SerializedName("adult")
        var adult: Boolean? = null,

        @field:SerializedName("homepage")
        var homepage: String? = null,

        @field:SerializedName("status")
        var status: String? = null,

        @field:SerializedName("images")
        var images: Images? = null

) : Parcelable{

    companion object CREATOR : Parcelable.Creator<Movie>{

        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
    @Ignore
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(VideoResponse::class.java.classLoader),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readParcelable(PeopleCredits::class.java.classLoader),
            parcel.createTypedArrayList(Genre),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.createTypedArrayList(ProductionCountry),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.createTypedArrayList(SpokenLanguage),
            parcel.createTypedArrayList(ProductionCompany),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Images::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(originalLanguage)
        parcel.writeString(imdbId)
        parcel.writeParcelable(videos, flags)
        parcel.writeValue(video)
        parcel.writeString(title)
        parcel.writeString(backdropPath)
        parcel.writeValue(revenue)
        parcel.writeParcelable(credits, flags)
        parcel.writeTypedList(genres)
        parcel.writeValue(popularity)
        parcel.writeTypedList(productionCountries)
        parcel.writeValue(id)
        parcel.writeValue(voteCount)
        parcel.writeValue(budget)
        parcel.writeString(overview)
        parcel.writeString(originalTitle)
        parcel.writeValue(runtime)
        parcel.writeString(posterPath)
        parcel.writeTypedList(spokenLanguages)
        parcel.writeTypedList(productionCompanies)
        parcel.writeString(releaseDate)
        parcel.writeValue(voteAverage)
        parcel.writeString(tagline)
        parcel.writeValue(adult)
        parcel.writeString(homepage)
        parcel.writeString(status)
        parcel.writeParcelable(images, flags)
    }

    fun getDirectors() : List<CrewItem?>? {
        return credits?.crew?.filter { it?.job == "Director" }
    }

    override fun describeContents(): Int {
        return 0
    }

}