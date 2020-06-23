package kr.ac.hansung.gyunggilocalmoneymap.domain.usecase

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.data.repository.NaverMapRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.repository.OpenApiRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.domain.base.UseCase
import kr.ac.hansung.gyunggilocalmoneymap.util.splitFirst

class GetMyLocationPlacesUseCase(
    private val openApiRepository: OpenApiRepository,
    private val naverMapRepository: NaverMapRepository
) : UseCase<Single<List<SHPlace>>, String> {

    override fun invoke(params: String): Single<List<SHPlace>> =
        naverMapRepository.getGeocode(params).map {
            it.results[0].region.area2.name.splitFirst()
        }.flatMap { si ->
            openApiRepository.getPlacesBySigun(si)
        }.subscribeOn(Schedulers.io())
}