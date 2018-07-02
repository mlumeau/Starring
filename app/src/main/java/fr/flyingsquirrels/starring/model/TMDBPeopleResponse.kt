package fr.flyingsquirrels.starring.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TMDBPeopleResponse(
        @SerializedName("page") var page: Int?,
        @SerializedName("total_results") var totalResults: Int?,
        @SerializedName("total_pages") var totalPages: Int?,
        @SerializedName("results") var people: List<Person>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.createTypedArrayList(Person))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(page)
        parcel.writeValue(totalResults)
        parcel.writeValue(totalPages)
        parcel.writeTypedList(people)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TMDBPeopleResponse> {
        override fun createFromParcel(parcel: Parcel): TMDBPeopleResponse {
            return TMDBPeopleResponse(parcel)
        }

        override fun newArray(size: Int): Array<TMDBPeopleResponse?> {
            return arrayOfNulls(size)
        }

        const val POPULAR= "people_popular"
    }
}