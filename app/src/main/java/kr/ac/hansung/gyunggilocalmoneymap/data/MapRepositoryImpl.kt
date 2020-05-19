package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.MapRemoteDataSource

class MapRepositoryImpl(
    private val mapRemoteDataSource: MapRemoteDataSource
) : MapRepository{

    override fun getPlaces(pIndex: String): Single<LocalMapResponse> = mapRemoteDataSource.getPlaces(pIndex)
}