package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.network.OpenApiService
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.LocalMapDataSource

class LocalMapDataSourceImpl(private val openApiService: OpenApiService) : LocalMapDataSource {

    override fun getPlaces(pIndex: String): Single<LocalMapResponse> = openApiService.getPlaces(pIndex)
}