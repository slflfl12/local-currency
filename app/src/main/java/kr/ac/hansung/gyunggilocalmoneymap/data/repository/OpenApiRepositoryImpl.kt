package kr.ac.hansung.gyunggilocalmoneymap.data.repository

import android.util.Log
import com.naver.maps.geometry.LatLng
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.data.local.mapper.MapEntityMapper
import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.OpenApiLocalDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.OpenApiRemoteDataSource
import kr.ac.hansung.gyunggilocalmoneymap.util.withinSightMarker

class OpenApiRepositoryImpl(
    private val openApiLocalDataSource: OpenApiLocalDataSource,
    private val openApiRemoteDataSource: OpenApiRemoteDataSource
) : OpenApiRepository {

    override val appVersion: String?
        get() = openApiLocalDataSource.appVersion

    override val loadedData: Int?
        get() = openApiLocalDataSource.loadedData?.toInt()

    override val pageLoadingSubject: BehaviorSubject<Int>
        get() = openApiRemoteDataSource.pageLoadingSubject

    override val loadedDataCompletedSubject = BehaviorSubject.createDefault(false)

    override fun getPlacesByIndex(pIndex: String): Single<List<SHPlace>> =
        openApiRemoteDataSource.getPlacesByIndex(pIndex)

    override fun saveAll(): Completable {
        return openApiLocalDataSource.deleteAll()
            .andThen(Observable.fromIterable(1..270))
            .flatMapSingle { page ->
                pageLoadingSubject.onNext(page)
                openApiRemoteDataSource.getPlacesByIndex(page.toString())
            }
            .concatMapCompletable {
                    openApiLocalDataSource.insertMaps(it)
                }.doOnTerminate {
                    openApiLocalDataSource.appVersion =
                        BuildConfig.VERSION_NAME
                }
            }

    override fun saveData(): Completable =
        Observable.fromIterable(loadedData?.rangeTo(270))
                .flatMapSingle { page ->
                pageLoadingSubject.onNext(page)
                    openApiLocalDataSource.loadedData = page.toString()
                openApiRemoteDataSource.getPlacesByIndex(page.toString())
            }
            .concatMapCompletable(openApiLocalDataSource::insertMaps)
            .doOnComplete {
                openApiLocalDataSource.appVersion =
                    BuildConfig.VERSION_NAME
            }



    override fun getAllPlaces(): Observable<SHPlace> {
        return openApiLocalDataSource.getMapEntities()
            .map { it.map(MapEntityMapper::mapToSHPlace) }
            .flatMapObservable{
                Observable.fromIterable(it)
            }
    }

    override fun getAllPlacesPrev(): Single<List<SHPlace>> {
        return openApiLocalDataSource.getMapEntities()
            .map { it.map(MapEntityMapper::mapToSHPlace)}
    }

    override fun getNearByPlaces(currentLatLng: LatLng): Single<List<SHPlace>> =
        openApiLocalDataSource.getMapEntities()
            .map { it.map(MapEntityMapper::mapToSHPlace)}
            .map { it.filter{ it.withinSightMarker(currentLatLng)} }

    override fun getPlacesBySigun(si: String): Single<List<SHPlace>> {
        return openApiLocalDataSource.getMapEntitiesBySigun(si)
            .map {it.map(MapEntityMapper::mapToSHPlace) }
    }

    override fun deleteAll(): Completable {
        return openApiLocalDataSource.deleteAll()
    }
}

