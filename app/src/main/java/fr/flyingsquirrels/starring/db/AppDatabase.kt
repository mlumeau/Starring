package fr.flyingsquirrels.starring.db;

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.flyingsquirrels.starring.dao.FavoritesDao
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.model.TVShow
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

@Database(entities = [(Movie::class), (TVShow::class), (Person::class)], version = 1)
@TypeConverters(Converters::class)
abstract class StarringDB : RoomDatabase() {

        abstract fun favoritesDao(): FavoritesDao
}

val DatabaseModule : Module = applicationContext {

    bean {
        Room.databaseBuilder(get(),
                StarringDB::class.java, "starring.db")
                .build()

    }
}
