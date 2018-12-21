package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PeopleCredits(

        @field:SerializedName("cast")
	val cast: List<CastItem?>? = null,

        @field:SerializedName("crew")
	val crew: List<CrewItem?>? = null
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

	companion object CREATOR : Parcelable.Creator<PeopleCredits> {
		override fun createFromParcel(parcel: Parcel): PeopleCredits {
			return PeopleCredits(parcel)
		}

		override fun newArray(size: Int): Array<PeopleCredits?> {
			return arrayOfNulls(size)
		}
	}
}