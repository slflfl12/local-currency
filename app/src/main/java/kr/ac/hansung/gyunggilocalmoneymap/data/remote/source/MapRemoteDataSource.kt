package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse

interface MapRemoteDataSource {

    fun getPlaces(pIndex: String) : Single<LocalMapResponse>
}