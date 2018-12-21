package fr.flyingsquirrels.starring.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import fr.flyingsquirrels.starring.model.*
import fr.flyingsquirrels.starring.utils.fromJson
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

object Converters : KoinComponent{
    val gson : Gson by inject()

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
    fun peopleCreditsFromString(data: String): PeopleCredits? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun peopleCreditsToString(data: PeopleCredits?): String {
        return gson.toJson(data)
    }


    @TypeConverter
    @JvmStatic
    fun mediaCreditsFromString(data: String): MediaCredits? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun mediaCreditsToString(data: MediaCredits?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun mediaCreditListFromString(data: String): List<MediaCredit?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun mediaCreditListToString(data: List<MediaCredit?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun genreListFromString(data: String): List<Genre?>? {
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
    fun tvNetworkListFromString(data: String): List<TVNetwork?>? {
        return gson.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun tvNetworkListToString(data: List<TVNetwork?>?): String {
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

}