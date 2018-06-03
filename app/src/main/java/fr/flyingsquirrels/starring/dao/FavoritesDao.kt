package fr.flyingsquirrels.starring.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import fr.flyingsquirrels.starring.model.TMDBMovie
import fr.flyingsquirrels.starring.model.TMDBTVShow
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface FavoritesDao {

    @Query("SELECT * from TMDBMovie")
    fun getFavoriteMovies(): Flowable<List<TMDBMovie>>

    @Query("SELECT * from TMDBMovie where id=:id LIMIT 1")
    fun getFavoriteMovieWithId(id: Int?): Maybe<TMDBMovie>

    @Insert(onConflict = REPLACE)
    fun insertFavoriteMovie(movie: TMDBMovie)

    @Delete
    fun deleteFavoriteMovie(movie: TMDBMovie)

    @Query("SELECT * from TMDBTVShow")
    fun getFavoriteTVShows(): Flowable<List<TMDBTVShow>>

    @Query("SELECT * from TMDBTVShow where id=:id LIMIT 1")
    fun getFavoriteTVShowWithId(id: Int?): Maybe<TMDBTVShow>

    @Insert(onConflict = REPLACE)
    fun insertFavoriteTVShow(TVShow: TMDBTVShow)

    @Delete
    fun deleteFavoriteTVShow(TVShow: TMDBTVShow)
}
