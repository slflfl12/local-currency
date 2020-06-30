package kr.ac.hansung.gyunggilocalmoneymap.data.repository

import com.naver.maps.geometry.LatLng
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

interface OpenApiRepository {

    val appVersion: String?

    val loadedData: Int?

    val pageLoadingSubject: BehaviorSubject<Int>

    val loadedDataCompletedSubject: BehaviorSubject<Boolean>

    fun getPlacesByIndex(pIndex: String): Single<List<SHPlace>>

    fun getPlacesBySigun(si: String) : Single<List<SHPlace>>

    fun getAllPlaces() : Observable<SHPlace>

    fun getAllPlacesPrev(): Single<List<SHPlace>>

    fun getNearByPlaces(currentLatLng: LatLng) : Single<List<SHPlace>>

    fun saveAll(): Completable

    fun saveData(): Completable

    fun deleteAll(): Completable
}