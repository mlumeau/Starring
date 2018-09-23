package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TVShowResponse(
        @SerializedName("page")
        val page: Int? = null,

        @SerializedName("results")
        val results: List<TVShow>? = null

) : Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.createTypedArrayList(TVShow)
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(page)
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieResponse> {
        override fun createFromParcel(parcel: Parcel): MovieResponse {
            return MovieResponse(parcel)
        }

        override fun newArray(size: Int): Array<MovieResponse?> {
            return arrayOfNulls(size)
        }

        const val TOP_RATED= "tv_top_rated"
        const val POPULAR= "tv_popular"
        const val ON_THE_AIR= "tv_on_the_air"
        const val AIRING_TODAY= "tv_airing_today"
    }
}