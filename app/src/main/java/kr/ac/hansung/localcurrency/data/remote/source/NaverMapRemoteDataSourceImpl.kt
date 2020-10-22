package kr.ac.hansung.localcurrency.data.remote.source

import io.reactivex.Single
import kr.ac.hansung.localcurrency.data.remote.model.GeocodeResponse
import kr.ac.hansung.localcurrency.data.remote.network.ReverseGeoCodeService

class NaverMapRemoteDataSourceImpl(
        private val reverseGeoCodeService: ReverseGeoCodeService
) : NaverMapRemoteDataSource {

    override fun getGeocode(coords: String): Single<GeocodeResponse> = reverseGeoCodeService.getGeocode(coords)
}