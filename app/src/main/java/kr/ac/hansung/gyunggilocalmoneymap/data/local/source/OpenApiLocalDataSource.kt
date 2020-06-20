package kr.ac.hansung.gyunggilocalmoneymap.data.local.source

import io.reactivex.Completable
import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.local.model.MapEntity
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

interface OpenApiLocalDataSource {

    var appVersion: String?

    fun insertMaps(places: List<SHPlace>) : Completable

    fun getMapEntities(): Single<List<MapEntity>>

    fun getMapEntitiesBySigun(si: String): Single<List<MapEntity>>

    fun deleteAll(): Completable
}