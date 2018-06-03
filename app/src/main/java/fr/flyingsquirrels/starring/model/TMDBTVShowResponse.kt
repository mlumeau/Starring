package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TMDBTVShowResponse(
        @SerializedName("page")
        val page: Int? = null,

        @SerializedName("results")
        val results: List<TMDBTVShow>? = null

) : Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.createTypedArrayList(TMDBTVShow)
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(page)
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

        const val TOP_RATED= "tv_top_rated"
        const val POPULAR= "tv_popular"
        const val ON_THE_AIR= "tv_on_the_air"
        const val AIRING_TODAY= "tv_airing_today"
    }
}