package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

interface OpenApiRepository {

    val appVersion: String?

    val pageLoadingSubject: BehaviorSubject<Float>

    fun getPlacesByIndex(pIndex: String): Single<List<SHPlace>>

    fun getPlacesBySigun(si: String) : Single<List<SHPlace>>

    fun getMapEntities() : Single<List<SHPlace>>

    fun saveAll(): Completable

    fun deleteAll(): Completable
}