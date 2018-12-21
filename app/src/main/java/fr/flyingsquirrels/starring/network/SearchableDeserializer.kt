package fr.flyingsquirrels.starring.network

import android.util.Log
import com.google.gson.*
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.model.Searchable
import fr.flyingsquirrels.starring.model.Searchable.Companion.MOVIE
import fr.flyingsquirrels.starring.model.Searchable.Companion.PERSON
import fr.flyingsquirrels.starring.model.Searchable.Companion.TV
import fr.flyingsquirrels.starring.model.TVShow
import java.lang.reflect.Type


class SearchableDeserializer : JsonDeserializer<Searchable> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Searchable? {
        val gson = Gson()
        var item: Searchable? = null
        val rootObject = json.asJsonObject
        val type = rootObject.get("media_type").asString
        when (type) {
            MOVIE -> item = gson.fromJson<Movie>(json, Movie::class.java)
            TV -> item = gson.fromJson<TVShow>(json, TVShow::class.java)
            PERSON -> item = gson.fromJson<Person>(json, Person::class.java)
            else -> Log.d("TAG", "Invalid type: $type")
        }
        return item
    }
}