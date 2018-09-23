package fr.flyingsquirrels.starring.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import fr.flyingsquirrels.starring.model.Movie
import fr.flyingsquirrels.starring.model.Person
import fr.flyingsquirrels.starring.model.TVShow
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface FavoritesDao {

    @Query("SELECT * from Movie")
    fun getFavoriteMovies(): Flowable<List<Movie>>

    @Query("SELECT * from Movie where id=:id LIMIT 1")
    fun getFavoriteMovieWithId(id: Int?): Maybe<Movie>

    @Insert(onConflict = REPLACE)
    fun insertFavoriteMovie(movie: Movie)

    @Delete
    fun deleteFavoriteMovie(movie: Movie)

    @Query("SELECT * from TVShow")
    fun getFavoriteTVShows(): Flowable<List<TVShow>>

    @Query("SELECT * from TVShow where id=:id LIMIT 1")
    fun getFavoriteTVShowWithId(id: Int?): Maybe<TVShow>

    @Insert(onConflict = REPLACE)
    fun insertFavoriteTVShow(TVShow: TVShow)

    @Delete
    fun deleteFavoriteTVShow(TVShow: TVShow)


    @Query("SELECT * from Person")
    fun getFavoritePeople(): Flowable<List<Person>>

    @Query("SELECT * from Person where id=:id LIMIT 1")
    fun getFavoritePersonWithId(id: Int?): Maybe<Person>

    @Insert(onConflict = REPLACE)
    fun insertFavoritePerson(TVShow: Person)

    @Delete
    fun deleteFavoritePerson(TVShow: Person)
}
