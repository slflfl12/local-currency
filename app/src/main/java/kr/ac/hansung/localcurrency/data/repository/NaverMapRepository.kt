package kr.ac.hansung.localcurrency.data.repository

import io.reactivex.Single
import kr.ac.hansung.localcurrency.data.remote.model.GeocodeResponse

interface NaverMapRepository {

    fun getGeocode(coords: String): Single<GeocodeResponse>
}