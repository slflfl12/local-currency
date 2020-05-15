package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.LocalMapDataSource

class LocalMapRepositoryImpl(
    private val localMapDataSource: LocalMapDataSource
) : LocalMapRepository{

    override fun getAllPlace(): Single<LocalMapResponse> = localMapDataSource.getPlaces()
}