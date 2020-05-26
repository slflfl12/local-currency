package kr.ac.hansung.gyunggilocalmoneymap.data.local.source

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.data.local.dao.MapDao
import kr.ac.hansung.gyunggilocalmoneymap.data.local.mapper.MapEntityMapper
import kr.ac.hansung.gyunggilocalmoneymap.data.local.model.MapEntity
import kr.ac.hansung.gyunggilocalmoneymap.data.local.pref.PreferencesHelper
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse.RegionMnyFacltStu.Place
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

class MapLocalDataSourceImpl(
    private val mapDao: MapDao,
    private val pref: PreferencesHelper
) : MapLocalDataSource {

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


    override fun deleteAll(): Completable {
        return mapDao.deleteAll()
    }
}