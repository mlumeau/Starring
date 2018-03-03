package com.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TMDBMovieResponse(@field:SerializedName("results")  var results: List<TMDBMovie>) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(TMDBMovie)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TMDBMovieResponse> {
        override fun createFromParcel(parcel: Parcel): TMDBMovieResponse {
            return TMDBMovieResponse(parcel)
        }

        override fun newArray(size: Int): Array<TMDBMovieResponse?> {
            return arrayOfNulls(size)
        }

        const val TOP_RATED= "top_rated"
        const val POPULAR= "popular"
        const val NOW_PLAYING= "now_playing"
        const val UPCOMING= "upcoming"
    }
}