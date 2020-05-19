package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.network.OpenApiService

class MapRemoteDataSourceImpl(private val openApiService: OpenApiService) : MapRemoteDataSource {

    override fun getPlaces(pIndex: String): Single<LocalMapResponse> = openApiService.getPlaces(pIndex)
}