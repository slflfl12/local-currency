package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.GeocodeResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.network.ReverseGeoCodeService

class NaverMapRemoteDataSourceImpl(
    private val reverseGeoCodeService: ReverseGeoCodeService
) : NaverMapRemoteDataSource {

    override fun getGeocode(coords: String): Single<GeocodeResponse> = reverseGeoCodeService.getGeocode(coords)
}