package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Genre(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(name)
		parcel.writeValue(id)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Genre> {
		override fun createFromParcel(parcel: Parcel): Genre {
			return Genre(parcel)
		}

		override fun newArray(size: Int): Array<Genre?> {
			return arrayOfNulls(size)
		}
	}
}