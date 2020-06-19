package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.GeocodeResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.NaverMapRemoteDataSource

class NaverMapRepositoryImpl(
    private val naverMapRemoteDataSource: NaverMapRemoteDataSource
) : NaverMapRepository{

    override fun getGeocode(coords: String): Single<GeocodeResponse> = naverMapRemoteDataSource.getGeocode(coords)


}