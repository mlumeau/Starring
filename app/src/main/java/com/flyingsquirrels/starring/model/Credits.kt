package com.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Credits(

        @field:SerializedName("cast")
	val cast: List<com.flyingsquirrels.starring.model.CastItem?>? = null,

        @field:SerializedName("crew")
	val crew: List<com.flyingsquirrels.starring.model.CrewItem?>? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.createTypedArrayList(CastItem),
			parcel.createTypedArrayList(CrewItem)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeTypedList(cast)
		parcel.writeTypedList(crew)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Credits> {
		override fun createFromParcel(parcel: Parcel): Credits {
			return Credits(parcel)
		}

		override fun newArray(size: Int): Array<Credits?> {
			return arrayOfNulls(size)
		}
	}
}