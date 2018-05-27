package fr.flyingsquirrels.starring.model

import android.arch.persistence.room.Entity
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Entity
data class TMDBTVShow(
        @SerializedName("original_language")
        val originalLanguage: String? = null,

        @SerializedName("number_of_episodes")
        val numberOfEpisodes: Int? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("backdrop_path")
        val backdropPath: String? = null,

        @SerializedName("credits")
        val credits: Credits? = null,

        @SerializedName("popularity")
        val popularity: Double? = null,

        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("number_of_seasons")
        val numberOfSeasons: Int? = null,

        @SerializedName("vote_count")
        val voteCount: Int? = null,

        @SerializedName("first_air_date")
        val firstAirDate: String? = null,

        @SerializedName("overview")
        val overview: String? = null,

        @SerializedName("seasons")
        val seasons: List<SeasonsItem?>? = null,

        @SerializedName("images")
        val images: Images? = null,

        @SerializedName("created_by")
        val createdBy: List<CreatedByItem?>? = null,

        @SerializedName("poster_path")
        val posterPath: String? = null,

        @SerializedName("origin_country")
        val originCountry: List<String?>? = null,

        @SerializedName("original_name")
        val originalName: String? = null,

        @SerializedName("vote_average")
        val voteAverage: Int? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("episode_run_time")
        val episodeRunTime: List<Int?>? = null,

        @SerializedName("in_production")
        val inProduction: Boolean? = null,

        @SerializedName("last_air_date")
        val lastAirDate: String? = null,

        @SerializedName("homepage")
        val homepage: String? = null,

        @SerializedName("status")
        val status: String? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Credits::class.java.classLoader),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(SeasonsItem),
            parcel.readParcelable(Images::class.java.classLoader),
            parcel.createTypedArrayList(CreatedByItem),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.createIntArray().asList(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(originalLanguage)
        parcel.writeValue(numberOfEpisodes)
        parcel.writeString(type)
        parcel.writeString(backdropPath)
        parcel.writeParcelable(credits, flags)
        parcel.writeValue(popularity)
        parcel.writeValue(id)
        parcel.writeValue(numberOfSeasons)
        parcel.writeValue(voteCount)
        parcel.writeString(firstAirDate)
        parcel.writeString(overview)
        parcel.writeParcelable(images, flags)
        parcel.writeString(posterPath)
        parcel.writeStringList(originCountry)
        parcel.writeString(originalName)
        parcel.writeValue(voteAverage)
        parcel.writeString(name)
        parcel.writeValue(inProduction)
        parcel.writeString(lastAirDate)
        parcel.writeString(homepage)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TMDBTVShow> {
        override fun createFromParcel(parcel: Parcel): TMDBTVShow {
            return TMDBTVShow(parcel)
        }

        override fun newArray(size: Int): Array<TMDBTVShow?> {
            return arrayOfNulls(size)
        }
    }
}