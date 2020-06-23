package kr.ac.hansung.gyunggilocalmoneymap.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

interface OpenApiRepository {

    val appVersion: String?

    val loadedData: Int?

    val pageLoadingSubject: BehaviorSubject<Int>

    fun getPlacesByIndex(pIndex: String): Single<List<SHPlace>>

    fun getPlacesBySigun(si: String) : Single<List<SHPlace>>

    fun getMapEntities() : Single<List<SHPlace>>

    fun saveAll(): Completable

    fun saveData(): Completable

    fun deleteAll(): Completable
}