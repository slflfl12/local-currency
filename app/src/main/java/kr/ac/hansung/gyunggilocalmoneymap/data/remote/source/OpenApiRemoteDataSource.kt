package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

interface OpenApiRemoteDataSource {

    val pageLoadingSubject: BehaviorSubject<Int>

    fun getPlacesByIndex(pIndex: String) : Single<List<SHPlace>>




}