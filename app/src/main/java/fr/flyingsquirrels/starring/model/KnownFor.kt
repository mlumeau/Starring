package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class KnownFor(
        @SerializedName("vote_average") var voteAverage: Double?,
        @SerializedName("vote_count") var voteCount: Int?,
        @SerializedName("id") var id: Int?,
        @SerializedName("video") var video: Boolean?,
        @SerializedName("media_type") var mediaType: String?,
        @SerializedName("title") var title: String?,
        @SerializedName("popularity") var popularity: Double?,
        @SerializedName("poster_path") var posterPath: String?,
        @SerializedName("original_language") var originalLanguage: String?,
        @SerializedName("original_title") var originalTitle: String?,
        @SerializedName("genre_ids") var genreIds: List<Int?>?,
        @SerializedName("backdrop_path") var backdropPath: String?,
        @SerializedName("adult") var adult: Boolean?,
        @SerializedName("overview") var overview: String?,
        @SerializedName("release_date") var releaseDate: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createIntArray().toList(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(voteAverage)
        parcel.writeValue(voteCount)
        parcel.writeValue(id)
        parcel.writeValue(video)
        parcel.writeString(mediaType)
        parcel.writeString(title)
        parcel.writeValue(popularity)
        parcel.writeString(posterPath)
        parcel.writeString(originalLanguage)
        parcel.writeString(originalTitle)
        parcel.writeList(genreIds)
        parcel.writeString(backdropPath)
        parcel.writeValue(adult)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KnownFor> {
        override fun createFromParcel(parcel: Parcel): KnownFor {
            return KnownFor(parcel)
        }

        override fun newArray(size: Int): Array<KnownFor?> {
            return arrayOfNulls(size)
        }
    }
}