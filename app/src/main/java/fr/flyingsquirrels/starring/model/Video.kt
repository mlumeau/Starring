package fr.flyingsquirrels.starring.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Video(

		@field:SerializedName("site")
		val site: String? = null,

		@field:SerializedName("size")
		val size: Int? = null,

		@field:SerializedName("iso_3166_1")
		val iso31661: String? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("id")
		val id: String? = null,

		@field:SerializedName("type")
		val type: String? = null,

		@field:SerializedName("iso_639_1")
		val iso6391: String? = null,

		@field:SerializedName("key")
		val key: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(site)
		parcel.writeValue(size)
		parcel.writeString(iso31661)
		parcel.writeString(name)
		parcel.writeString(id)
		parcel.writeString(type)
		parcel.writeString(iso6391)
		parcel.writeString(key)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Video> {
		override fun createFromParcel(parcel: Parcel): Video {
			return Video(parcel)
		}

		override fun newArray(size: Int): Array<Video?> {
			return arrayOfNulls(size)
		}
	}
}