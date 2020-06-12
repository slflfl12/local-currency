package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.GeocodeResponse

interface NaverMapRepository {

    fun getGeocode(coords: String) : Single<GeocodeResponse>
}