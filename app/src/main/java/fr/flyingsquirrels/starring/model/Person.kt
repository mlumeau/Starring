package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Person(
        @SerializedName("popularity") var popularity: Double? = null,
        @PrimaryKey
        @SerializedName("id") var id: Int? = null,
        @SerializedName("profile_path") var profilePath: String? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("birthday") var birthday: String? = null,
        @SerializedName("deathday") var deathday: String? = null,
        @SerializedName("biography") var biography: String? = null,
        @SerializedName("images") var images: Images? = null,
        @SerializedName("combined_credits") var mediaCredits: MediaCredits? = null,
        @SerializedName("adult") var adult: Boolean? = null
        ) : Parcelable, Searchable(PERSON) {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Images::class.java.classLoader),
            null,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(popularity)
        parcel.writeValue(id)
        parcel.writeString(profilePath)
        parcel.writeString(name)
        parcel.writeString(birthday)
        parcel.writeString(deathday)
        parcel.writeString(biography)
        parcel.writeParcelable(images,flags)
        //parcel.writeParcelable(mediaCredits,flags)
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