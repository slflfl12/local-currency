package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse

interface MapRepository {

    val appVersion: String?

    fun getPlaces(pIndex: String): Single<LocalMapResponse>

    fun saveAll(): Completable

    fun deleteAll(): Completable
}