package fr.flyingsquirrels.starring.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Entity
data class TMDBTVShow(
        @SerializedName("original_language")
        var originalLanguage: String? = null,

        @SerializedName("number_of_episodes")
        var numberOfEpisodes: Int? = null,

        @SerializedName("type")
        var type: String? = null,

        @SerializedName("backdrop_path")
        var backdropPath: String? = null,

        @SerializedName("credits")
        var credits: Credits? = null,

        @SerializedName("popularity")
        var popularity: Double? = null,

        @PrimaryKey
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("number_of_seasons")
        var numberOfSeasons: Int? = null,

        @SerializedName("vote_count")
        var voteCount: Int? = null,

        @SerializedName("first_air_date")
        var firstAirDate: String? = null,

        @SerializedName("overview")
        var overview: String? = null,

        @SerializedName("seasons")
        var seasons: List<Season?>? = null,

        @SerializedName("images")
        var images: Images? = null,

        @SerializedName("created_by")
        var createdBy: List<CreatedByItem?>? = null,

        @SerializedName("poster_path")
        var posterPath: String? = null,

        @SerializedName("origin_country")
        var originCountry: List<String?>? = null,

        @SerializedName("original_name")
        var originalName: String? = null,

        @SerializedName("vote_average")
        var voteAverage: Double? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("episode_run_time")
        var episodeRunTime: List<Int?>? = null,

        @SerializedName("in_production")
        var inProduction: Boolean? = null,

        @SerializedName("last_air_date")
        var lastAirDate: String? = null,

        @SerializedName("homepage")
        var homepage: String? = null,

        @SerializedName("status")
        var status: String? = null

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
            parcel.createTypedArrayList(Season),
            parcel.readParcelable(Images::class.java.classLoader),
            parcel.createTypedArrayList(CreatedByItem),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
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