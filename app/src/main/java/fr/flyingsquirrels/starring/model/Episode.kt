package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Episode(
        @SerializedName("air_date") var airDate: String?,
        @SerializedName("crew") var crew: List<CrewItem?>?,
        @SerializedName("episode_number") var episodeNumber: Int?,
        @SerializedName("guest_stars") var guestStars: List<CrewItem?>?,
        @SerializedName("name") var name: String?,
        @SerializedName("overview") var overview: String?,
        @SerializedName("id") var id: Int?,
        @SerializedName("production_code") var productionCode: String?,
        @SerializedName("season_number") var seasonNumber: Int?,
        @SerializedName("still_path") var stillPath: String?,
        @SerializedName("vote_average") var voteAverage: Double?,
        @SerializedName("vote_count") var voteCount: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArrayList(CrewItem),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.createTypedArrayList(CrewItem),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(airDate)
        parcel.writeTypedList(crew)
        parcel.writeValue(episodeNumber)
        parcel.writeTypedList(guestStars)
        parcel.writeString(name)
        parcel.writeString(overview)
        parcel.writeValue(id)
        parcel.writeString(productionCode)
        parcel.writeValue(seasonNumber)
        parcel.writeString(stillPath)
        parcel.writeValue(voteAverage)
        parcel.writeValue(voteCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Episode> {
        override fun createFromParcel(parcel: Parcel): Episode {
            return Episode(parcel)
        }

        override fun newArray(size: Int): Array<Episode?> {
            return arrayOfNulls(size)
        }
    }
}