package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Season(@SerializedName("air_date")
                       val airDate: String? = null,
                  @SerializedName("overview")
                       val overview: String? = null,
                  @SerializedName("episode_count")
                       val episodeCount: Int? = null,
                  @SerializedName("episodes")
                    var episodes: List<Episode?>?,
                  @SerializedName("name")
                       val name: String? = null,
                  @SerializedName("season_number")
                       val seasonNumber: Int? = null,
                  @SerializedName("id")
                       val id: Int? = null,
                  @SerializedName("poster_path")
                       val posterPath: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.createTypedArrayList(Episode),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(airDate)
        parcel.writeString(overview)
        parcel.writeValue(episodeCount)
        parcel.writeTypedList(episodes)
        parcel.writeString(name)
        parcel.writeValue(seasonNumber)
        parcel.writeValue(id)
        parcel.writeString(posterPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Season> {
        override fun createFromParcel(parcel: Parcel): Season {
            return Season(parcel)
        }

        override fun newArray(size: Int): Array<Season?> {
            return arrayOfNulls(size)
        }
    }
}