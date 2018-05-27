package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CreatedByItem(@SerializedName("name")
                         val name: String? = null,
                         @SerializedName("profile_path")
                         val profilePath: String? = null,
                         @SerializedName("id")
                         val id: Int? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(profilePath)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreatedByItem> {
        override fun createFromParcel(parcel: Parcel): CreatedByItem {
            return CreatedByItem(parcel)
        }

        override fun newArray(size: Int): Array<CreatedByItem?> {
            return arrayOfNulls(size)
        }
    }
}