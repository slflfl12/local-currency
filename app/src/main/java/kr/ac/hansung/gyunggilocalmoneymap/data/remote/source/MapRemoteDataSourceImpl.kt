package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.network.OpenApiService

class MapRemoteDataSourceImpl(private val openApiService: OpenApiService) : MapRemoteDataSource {

    override fun getPlaces(pIndex: String): Single<LocalMapResponse> = openApiService.getPlaces(pIndex)

    override fun saveAll(): Observable<Int> {
        return Observable.fromIterable(1..570)
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                Schedulers.single()
            )
    }
}