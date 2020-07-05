package kr.ac.hansung.localcurrency.data.remote.source

import io.reactivex.Single
import kr.ac.hansung.localcurrency.data.remote.model.GeocodeResponse

interface NaverMapRemoteDataSource {

    fun getGeocode(coords: String) : Single<GeocodeResponse>
}