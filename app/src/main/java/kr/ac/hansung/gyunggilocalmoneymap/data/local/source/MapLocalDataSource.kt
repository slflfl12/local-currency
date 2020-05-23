package kr.ac.hansung.gyunggilocalmoneymap.data.local.source

import io.reactivex.Completable
import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.local.model.MapEntity
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse.RegionMnyFacltStu.Place
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

interface MapLocalDataSource {

    var appVersion: String?

    fun insertMaps(places: List<SHPlace>) : Completable

    fun getMapEntities(): Single<List<MapEntity>>

    fun deleteAll(): Completable
}