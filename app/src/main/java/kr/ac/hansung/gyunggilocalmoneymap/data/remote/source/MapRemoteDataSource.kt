package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

interface MapRemoteDataSource {

    val pageLoadingSubject: BehaviorSubject<Float>

    fun getPlaces(pIndex: String) : Single<List<SHPlace>>

}