package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Images(

        @field:SerializedName("posters")
        val posters: List<Image?>? = null,

        @field:SerializedName("backdrops")
        val backdrops: List<Image?>? = null,

        @field:SerializedName("profiles")
        val profiles: List<Image?>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(Image),
            parcel.createTypedArrayList(Image),
            parcel.createTypedArrayList(Image)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(posters)
        parcel.writeTypedList(backdrops)
        parcel.writeTypedList(profiles)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Images> {
        override fun createFromParcel(parcel: Parcel): Images {
            return Images(parcel)
        }

        override fun newArray(size: Int): Array<Images?> {
            return arrayOfNulls(size)
        }
    }
}