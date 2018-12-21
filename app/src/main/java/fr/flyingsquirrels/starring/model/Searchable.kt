package fr.flyingsquirrels.starring.model

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

abstract class Searchable(
        @field:SerializedName("media_type")
        var mediaType: String? = null,

        @Ignore
        var _id: Int? = null
) {
    companion object {
        const val PERSON = "person"
        const val TV = "tv"
        const val MOVIE = "movie"
    }
}