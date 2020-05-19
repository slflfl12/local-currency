package kr.ac.hansung.gyunggilocalmoneymap.data.local.source

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.data.local.MapDao
import kr.ac.hansung.gyunggilocalmoneymap.data.local.mapper.MapLocalMapper
import kr.ac.hansung.gyunggilocalmoneymap.data.local.model.MapEntity
import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.MapLocalDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse.RegionMnyFacltStu.Place

class MapLocalDataSourceImpl(private val mapDao: MapDao) :
    MapLocalDataSource {


    override fun insertMaps(places: List<Place>): Completable {
        return Single.just(places)
            .map { it.map(MapLocalMapper::mapToLocal)}
            .flatMapCompletable {
                mapDao.insertMaps(it)
            }
            .subscribeOn(Schedulers.io())

    }

    override fun getMaps(): Single<List<MapEntity>> {
        return mapDao.getMaps()
            .subscribeOn(Schedulers.io())
    }
}