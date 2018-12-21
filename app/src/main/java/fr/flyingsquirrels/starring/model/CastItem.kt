package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CastItem(

	@field:SerializedName("cast_id")
	val castId: Int? = null,

	@field:SerializedName("character")
	val character: String? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("credit_id")
	val creditId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("profile_path")
	val profilePath: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("order")
	val order: Int? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(castId)
		parcel.writeString(character)
		parcel.writeValue(gender)
		parcel.writeString(creditId)
		parcel.writeString(name)
		parcel.writeString(profilePath)
		parcel.writeValue(id)
		parcel.writeValue(order)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<CastItem> {
		override fun createFromParcel(parcel: Parcel): CastItem {
			return CastItem(parcel)
		}

		override fun newArray(size: Int): Array<CastItem?> {
			return arrayOfNulls(size)
		}
	}
}