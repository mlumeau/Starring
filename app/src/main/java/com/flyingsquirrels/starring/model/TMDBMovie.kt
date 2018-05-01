package com.flyingsquirrels.starring.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TMDBMovie(

		@field:SerializedName("original_language")
		val originalLanguage: String? = null,

		@field:SerializedName("imdb_id")
		val imdbId: String? = null,

		@field:SerializedName("videos")
		val videos: VideoResponse? = null,

		@field:SerializedName("video")
		val video: Boolean? = null,

		@field:SerializedName("title")
		val title: String? = null,

		@field:SerializedName("backdrop_path")
		val backdropPath: String? = null,

		@field:SerializedName("revenue")
		val revenue: Int? = null,

		@field:SerializedName("credits")
		val credits: Credits? = null,

		@field:SerializedName("genres")
		val genres: List<GenresItem?>? = null,

		@field:SerializedName("popularity")
		val popularity: Double? = null,

		@field:SerializedName("production_countries")
		val productionCountries: List<ProductionCountriesItem?>? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("vote_count")
		val voteCount: Int? = null,

		@field:SerializedName("budget")
		val budget: Int? = null,

		@field:SerializedName("overview")
		val overview: String? = null,

		@field:SerializedName("original_title")
		val originalTitle: String? = null,

		@field:SerializedName("runtime")
		val runtime: Int? = null,

		@field:SerializedName("poster_path")
		val posterPath: String? = null,

		@field:SerializedName("spoken_languages")
		val spokenLanguages: List<SpokenLanguagesItem?>? = null,

		@field:SerializedName("production_companies")
		val productionCompanies: List<ProductionCompaniesItem?>? = null,

		@field:SerializedName("release_date")
		val releaseDate: String? = null,

		@field:SerializedName("vote_average")
		val voteAverage: Double? = null,

		@field:SerializedName("tagline")
		val tagline: String? = null,

		@field:SerializedName("adult")
		val adult: Boolean? = null,

		@field:SerializedName("homepage")
		val homepage: String? = null,

		@field:SerializedName("status")
		val status: String? = null
) : Parcelable{

	companion object CREATOR : Parcelable.Creator<TMDBMovie>{
		const val POSTER_URL_THUMBNAIL = "https://image.tmdb.org/t/p/w300"
		const val POSTER_URL_LARGE = "https://image.tmdb.org/t/p/w780"

		override fun createFromParcel(parcel: Parcel): TMDBMovie {
			return TMDBMovie(parcel)
		}

		override fun newArray(size: Int): Array<TMDBMovie?> {
			return arrayOfNulls(size)
		}
	}

	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString(),
			parcel.readParcelable(VideoResponse::class.java.classLoader),
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readParcelable(Credits::class.java.classLoader),
			parcel.createTypedArrayList(GenresItem),
			parcel.readValue(Double::class.java.classLoader) as? Double,
			parcel.createTypedArrayList(ProductionCountriesItem),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.createTypedArrayList(SpokenLanguagesItem),
			parcel.createTypedArrayList(ProductionCompaniesItem),
			parcel.readString(),
			parcel.readValue(Double::class.java.classLoader) as? Double,
			parcel.readString(),
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readString(),
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(originalLanguage)
		parcel.writeString(imdbId)
		parcel.writeParcelable(videos, flags)
		parcel.writeValue(video)
		parcel.writeString(title)
		parcel.writeString(backdropPath)
		parcel.writeValue(revenue)
		parcel.writeParcelable(credits, flags)
		parcel.writeTypedList(genres)
		parcel.writeValue(popularity)
		parcel.writeTypedList(productionCountries)
		parcel.writeValue(id)
		parcel.writeValue(voteCount)
		parcel.writeValue(budget)
		parcel.writeString(overview)
		parcel.writeString(originalTitle)
		parcel.writeValue(runtime)
		parcel.writeString(posterPath)
		parcel.writeTypedList(spokenLanguages)
		parcel.writeTypedList(productionCompanies)
		parcel.writeString(releaseDate)
		parcel.writeValue(voteAverage)
		parcel.writeString(tagline)
		parcel.writeValue(adult)
		parcel.writeString(homepage)
		parcel.writeString(status)
	}

	fun getDirectors() : List<CrewItem?>? {
		return credits?.crew?.filter { it?.job == "Director" }
	}

	override fun describeContents(): Int {
		return 0
	}

}