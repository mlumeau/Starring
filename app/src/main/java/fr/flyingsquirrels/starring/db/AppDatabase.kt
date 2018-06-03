package fr.flyingsquirrels.starring.db;

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import fr.flyingsquirrels.starring.dao.FavoritesDao
import fr.flyingsquirrels.starring.model.TMDBMovie
import fr.flyingsquirrels.starring.model.TMDBTVShow
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

@Database(entities = arrayOf(TMDBMovie::class, TMDBTVShow::class), version = 1)
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
