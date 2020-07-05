package kr.ac.hansung.localcurrency.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import kr.ac.hansung.localcurrency.data.local.model.MapEntity

@Dao
interface MapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMaps(places: List<MapEntity>) : Completable

    @Query("SELECT * FROM map")
    fun getMaps() : Single<List<MapEntity>>

    @Query("SELECT * FROM map WHERE sigun = :si")
    fun getMapsBySi(si: String) : Single<List<MapEntity>>

    @Query("SELECT * FROM map WHERE title LIKE '%' || :query || '%' OR sigun LIKE '%' || :query || '%' OR roadAddress LIKE '%' || :query || '%' ")
    fun getMapByQuery(query: String) : Single<List<MapEntity>>

    @Query("SELECT * FROM map WHERE ABS(:latitude - latitude_double) <= 0.5 / 109.958489129649955 AND ABS(:longitude - longitude_double) <= 0.5 / 88.74")
    fun getNearByMaps(latitude: Double, longitude: Double) : Single<List<MapEntity>>


    @Query("DELETE FROM map")
    fun deleteAll() : Completable


}