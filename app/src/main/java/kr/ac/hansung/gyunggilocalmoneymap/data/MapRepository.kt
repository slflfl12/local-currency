package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse

interface MapRepository {

    fun getPlaces(pIndex: String) : Single<LocalMapResponse>
}