package fr.flyingsquirrels.starring.model
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TVNetwork(
    @SerializedName("name")
    var name: String?,

    @SerializedName("id")
    var id: Int?,

    @SerializedName("logo_path")
    var logoPath: String?,

    @SerializedName("origin_country")
    var originCountry: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(id)
        parcel.writeString(logoPath)
        parcel.writeString(originCountry)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TVNetwork> {
        override fun createFromParcel(parcel: Parcel): TVNetwork {
            return TVNetwork(parcel)
        }

        override fun newArray(size: Int): Array<TVNetwork?> {
            return arrayOfNulls(size)
        }
    }
}