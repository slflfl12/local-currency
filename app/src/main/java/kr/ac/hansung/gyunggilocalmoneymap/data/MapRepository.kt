package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse

interface MapRepository {

    fun getPlaces(pIndex: String) : Single<LocalMapResponse>
}