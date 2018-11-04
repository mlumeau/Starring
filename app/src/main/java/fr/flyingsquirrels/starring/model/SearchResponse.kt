package fr.flyingsquirrels.starring.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(

        @SerializedName("page")
        val page: Int? = null,

        @SerializedName("results")
        val results: List<Searchable>? = null
)