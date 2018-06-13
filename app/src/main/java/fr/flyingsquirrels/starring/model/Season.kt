package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Season(@SerializedName("air_date")
                        var airDate: String? = null,
                  @SerializedName("overview")
                        var overview: String? = null,
                  @SerializedName("episode_count")
                        var episodeCount: Int? = null,
                  @SerializedName("episodes")
                        var episodes: List<Episode?>?,
                  @SerializedName("name")
                        var name: String? = null,
                  @SerializedName("season_number")
                        var seasonNumber: Int? = null,
                  @SerializedName("tvId")
                        var tvId: Int? = null,
                  @SerializedName("id")
                        var id: Int? = null,
                  @SerializedName("images")
                        var images: Images? = null,
                  @SerializedName("videos")
                        var videos: VideoResponse? = null,
                  @SerializedName("poster_path")
                        var posterPath: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.createTypedArrayList(Episode),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readParcelable(Images::class.java.classLoader),
            parcel.readParcelable(VideoResponse::class.java.classLoader),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(airDate)
        parcel.writeString(overview)
        parcel.writeValue(episodeCount)
        parcel.writeTypedList(episodes)
        parcel.writeString(name)
        parcel.writeValue(seasonNumber)
        parcel.writeValue(tvId)
        parcel.writeValue(id)
        parcel.writeParcelable(images, flags)
        parcel.writeParcelable(videos, flags)
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