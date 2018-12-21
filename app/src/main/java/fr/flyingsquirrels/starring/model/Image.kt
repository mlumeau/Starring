package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Image(
        @field:SerializedName("aspect_ratio")
        val aspect_ratio: Double? = null,

        @field:SerializedName("file_path")
        val file_path: String? = null,

        @field:SerializedName("height")
        val height: Int? = null,

        @field:SerializedName("iso_639_1")
        val iso_639_1: String? = null,

        @field:SerializedName("vote_average")
        val vote_average: Double? = null,

        @field:SerializedName("vote_count")
        val vote_count: Int? = null,

        @field:SerializedName("width")
        val width: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Int,
            parcel.readValue(Double::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(aspect_ratio)
        parcel.writeString(file_path)
        parcel.writeValue(height)
        parcel.writeString(iso_639_1)
        parcel.writeValue(vote_average)
        parcel.writeValue(vote_count)
        parcel.writeValue(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}