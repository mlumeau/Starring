package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SpokenLanguage(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("iso_639_1")
	val iso6391: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(name)
		parcel.writeString(iso6391)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<SpokenLanguage> {
		override fun createFromParcel(parcel: Parcel): SpokenLanguage {
			return SpokenLanguage(parcel)
		}

		override fun newArray(size: Int): Array<SpokenLanguage?> {
			return arrayOfNulls(size)
		}
	}
}