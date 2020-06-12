package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.GeocodeResponse

interface NaverMapRemoteDataSource {

    fun getGeocode(coords: String) : Single<GeocodeResponse>
}