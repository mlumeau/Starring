package fr.flyingsquirrels.starring.search

interface SearchInterface {

    fun search(query: String)

    companion object {
        const val QUERY = "query"
    }
}
