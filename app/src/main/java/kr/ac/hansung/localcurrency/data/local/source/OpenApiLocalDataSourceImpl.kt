package kr.ac.hansung.localcurrency.data.local.source

import io.reactivex.Completable
import io.reactivex.Single
import kr.ac.hansung.localcurrency.data.local.dao.MapDao
import kr.ac.hansung.localcurrency.data.local.mapper.MapEntityMapper
import kr.ac.hansung.localcurrency.data.local.model.MapEntity
import kr.ac.hansung.localcurrency.data.local.pref.PreferencesHelper
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace

class OpenApiLocalDataSourceImpl(
    private val mapDao: MapDao,
    private val pref: PreferencesHelper
) : OpenApiLocalDataSource {

    override var appVersion: String?
        get() = pref.appVersion
        set(value) {
            pref.appVersion = value
        }

    override var loadedData: String?
        get() = pref.loadedData
        set(value) {
            pref.loadedData = value
        }

    override fun insertMaps(places: List<SHPlace>): Completable {
        return Single.just(places)
            .map { it.map(MapEntityMapper::mapToLocal) }
            .flatMapCompletable { mapDao.insertMaps(it) }
    }

    override fun getMapEntities(): Single<List<MapEntity>> {
        return mapDao.getMaps()

    }

    override fun getMapEntitiesByQuery(query: String, latitude: Double, longitude: Double): Single<List<MapEntity>> = mapDao.getMapByQuery(query, latitude, longitude)

    override fun getMapEntitiesBySigun(si:String): Single<List<MapEntity>> = mapDao.getMapsBySi(si)

    override fun getNearByMaps(latitude: Double, longitude: Double, nearByValue: Double): Single<List<MapEntity>> {
        return mapDao.getNearByMaps(latitude, longitude, nearByValue)
    }

    override fun deleteAll(): Completable {
        return mapDao.deleteAll()
    }
}