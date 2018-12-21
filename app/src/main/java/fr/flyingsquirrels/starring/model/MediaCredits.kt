package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MediaCredits(

        @field:SerializedName("cast")
	val cast: List<MediaCredit?>? = null,

        @field:SerializedName("crew")
	val crew: List<MediaCredit?>? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.createTypedArrayList(MediaCredit),
			parcel.createTypedArrayList(MediaCredit)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeTypedList(cast)
		parcel.writeTypedList(crew)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<MediaCredits> {
		override fun createFromParcel(parcel: Parcel): MediaCredits {
			return MediaCredits(parcel)
		}

		override fun newArray(size: Int): Array<MediaCredits?> {
			return arrayOfNulls(size)
		}
	}
}