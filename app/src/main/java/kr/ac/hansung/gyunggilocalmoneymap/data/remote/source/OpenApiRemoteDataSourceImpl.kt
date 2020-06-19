package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.mapper.SHPlaceRemoteMapper
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.network.OpenApiService

class OpenApiRemoteDataSourceImpl(private val openApiService: OpenApiService) : OpenApiRemoteDataSource {

    override val pageLoadingSubject = BehaviorSubject.createDefault<Float>(0f)

    override fun getPlacesByIndex(pIndex: String): Single<List<SHPlace>> = openApiService.getPlaces(pIndex)
        .map {
            it.regionMnyFacltStus[1].places.filter { place ->
                place.latitude != null && place.longitude != null
            }
        }.map {
            it.map(SHPlaceRemoteMapper::mapToData)
        }



}