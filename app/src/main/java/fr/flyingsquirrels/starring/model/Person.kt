package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Person(
        @SerializedName("popularity") var popularity: Double?,
        @SerializedName("id") var id: Int?,
        @SerializedName("profile_path") var profilePath: String?,
        @SerializedName("name") var name: String?,
        @SerializedName("known_for") var knownFor: List<KnownFor?>?,
        @SerializedName("adult") var adult: Boolean?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(KnownFor),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(popularity)
        parcel.writeValue(id)
        parcel.readString()
        parcel.writeString(name)
        parcel.writeTypedList(knownFor)
        parcel.writeValue(adult)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}