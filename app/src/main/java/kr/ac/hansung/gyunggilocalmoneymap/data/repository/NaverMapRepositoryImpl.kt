package kr.ac.hansung.gyunggilocalmoneymap.data.repository

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.GeocodeResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.NaverMapRemoteDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.repository.NaverMapRepository

class NaverMapRepositoryImpl(
    private val naverMapRemoteDataSource: NaverMapRemoteDataSource
) : NaverMapRepository {

    override fun getGeocode(coords: String): Single<GeocodeResponse> = naverMapRemoteDataSource.getGeocode(coords)


}