package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

interface MapRepository {

    val appVersion: String?

    fun getPlaces(pIndex: String): Single<List<SHPlace>>

    fun saveAll(): Completable

    fun deleteAll(): Completable
}