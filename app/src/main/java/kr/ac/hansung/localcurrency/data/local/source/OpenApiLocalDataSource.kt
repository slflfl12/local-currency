package kr.ac.hansung.localcurrency.data.local.source

import io.reactivex.Completable
import io.reactivex.Single
import kr.ac.hansung.localcurrency.data.local.model.MapEntity
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace

interface OpenApiLocalDataSource {

    var appVersion: String?

    var loadedData: String?

    fun insertMaps(places: List<SHPlace>) : Completable

    fun getMapEntities(): Single<List<MapEntity>>

    fun getMapEntitiesBySigun(si: String): Single<List<MapEntity>>

    fun getMapEntitiesByQuery(query: String, latitude: Double, longitude: Double): Single<List<MapEntity>>

    fun getNearByMaps(latitude: Double, longitude: Double, nearByValue: Double): Single<List<MapEntity>>

    fun deleteAll(): Completable
}