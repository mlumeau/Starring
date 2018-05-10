package fr.flyingsquirrels.starring.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class VideoResponse(

		@field:SerializedName("results")
		val results: List<Video?>? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Video))

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeTypedList(results)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<VideoResponse> {
		override fun createFromParcel(parcel: Parcel): VideoResponse {
			return VideoResponse(parcel)
		}

		override fun newArray(size: Int): Array<VideoResponse?> {
			return arrayOfNulls(size)
		}
	}
}