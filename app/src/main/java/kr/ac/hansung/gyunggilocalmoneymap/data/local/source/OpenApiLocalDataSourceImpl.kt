package kr.ac.hansung.gyunggilocalmoneymap.data.local.source

import io.reactivex.Completable
import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.local.dao.MapDao
import kr.ac.hansung.gyunggilocalmoneymap.data.local.mapper.MapEntityMapper
import kr.ac.hansung.gyunggilocalmoneymap.data.local.model.MapEntity
import kr.ac.hansung.gyunggilocalmoneymap.data.local.pref.PreferencesHelper
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

class OpenApiLocalDataSourceImpl(
    private val mapDao: MapDao,
    private val pref: PreferencesHelper
) : OpenApiLocalDataSource {

    override var appVersion: String?
        get() = pref.appVersion
        set(value) {
            pref.appVersion = value
        }

    override fun insertMaps(places: List<SHPlace>): Completable {
        return Single.just(places)
            .map { it.map(MapEntityMapper::mapToLocal) }
            .flatMapCompletable { mapDao.insertMaps(it) }
    }

    override fun getMapEntities(): Single<List<MapEntity>> {
        return mapDao.getMaps()

    }

    override fun getMapEntitiesBySigun(si:String): Single<List<MapEntity>> = mapDao.getMapsBySi(si)


    override fun deleteAll(): Completable {
        return mapDao.deleteAll()
    }
}