package fr.flyingsquirrels.starring.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.flyingsquirrels.starring.model.*

object Converters{
    val gson : Gson = Gson()

    @TypeConverter
    @JvmStatic
    fun videoResponseFromString(data: String): VideoResponse? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun videoResponseToString(data: VideoResponse?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun creditsFromString(data: String): Credits? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun creditsToString(data: Credits?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun genresItemListFromString(data: String): List<GenresItem?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun genresItemListToString(data: List<GenresItem?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun productionCountriesItemListFromString(data: String): List<ProductionCountriesItem?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun productionCountriesItemListToString(data: List<ProductionCountriesItem?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun spokenLanguagesItemListFromString(data: String): List<SpokenLanguagesItem?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun spokenLanguagesItemListToString(data: List<SpokenLanguagesItem?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun productionCompaniesItemListFromString(data: String): List<ProductionCompaniesItem?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun productionCompaniesItemListToString(data: List<ProductionCompaniesItem?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun imagesFromString(data: String): Images? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun imagesToString(data: Images?): String {
        return gson.toJson(data)
    }

    private inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}