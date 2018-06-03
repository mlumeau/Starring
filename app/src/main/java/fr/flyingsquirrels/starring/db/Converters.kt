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
    fun genreistFromString(data: String): List<Genre?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun genreListToString(data: List<Genre?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun productionCountryListFromString(data: String): List<ProductionCountry?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun productionCountryListToString(data: List<ProductionCountry?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun spokenLanguageListFromString(data: String): List<SpokenLanguage?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun spokenLanguageListToString(data: List<SpokenLanguage?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun productionCompanyListFromString(data: String): List<ProductionCompany?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun productionCompanyListToString(data: List<ProductionCompany?>?): String {
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

    @TypeConverter
    @JvmStatic
    fun createdByItemListFromString(data: String): List<CreatedByItem?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun createdByItemListToString(data: List<CreatedByItem?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun seasonListFromString(data: String): List<Season?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun seasonListToString(data: List<Season?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun stringListFromString(data: String): List<String?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun stringListToString(data: List<String?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun intListFromString(data: String): List<Int?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(data: List<Int?>?): String {
        return gson.toJson(data)
    }



    private inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}