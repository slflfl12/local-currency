package kr.ac.hansung.gyunggilocalmoneymap.data

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.data.local.mapper.MapEntityMapper
import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.OpenApiLocalDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.OpenApiRemoteDataSource

class OpenApiRepositoryImpl(
    private val openApiLocalDataSource: OpenApiLocalDataSource,
    private val openApiRemoteDataSource: OpenApiRemoteDataSource
) : OpenApiRepository {

    override val appVersion: String?
        get() = openApiLocalDataSource.appVersion

    override val pageLoadingSubject: BehaviorSubject<Float>
        get() = openApiRemoteDataSource.pageLoadingSubject

    override fun getPlacesByIndex(pIndex: String): Single<List<SHPlace>> =
        openApiRemoteDataSource.getPlacesByIndex(pIndex)

    override fun saveAll(): Completable {
        return openApiLocalDataSource.deleteAll()
            .andThen(Observable.fromIterable(1..570))
            .flatMapSingle { page ->
                pageLoadingSubject.onNext((page / 570).toFloat())
                Log.d(
                    "sh loading repository",
                    (page / 570f).toString()
                )
                openApiRemoteDataSource.getPlacesByIndex(page.toString())
            }.concatMapCompletable {
                openApiLocalDataSource.insertMaps(it)
            }.doOnTerminate {
                openApiLocalDataSource.appVersion =
                    BuildConfig.VERSION_NAME
                Log.d("sh caching", "sh caching")
            }
    }

    override fun getMapEntities(): Single<List<SHPlace>> {
        return openApiLocalDataSource.getMapEntities()
            .map { it.map(MapEntityMapper::mapToSHPlace) }
            .doOnSubscribe {
                Log.d("sh 처리중", "sh 처리중")
            }
    }

    override fun getPlacesBySi(si: String): Single<List<SHPlace>> {
        return openApiLocalDataSource.getMapEntitiesBySi(si)
            .map {it.map(MapEntityMapper::mapToSHPlace) }
    }

    override fun deleteAll(): Completable {
        return openApiLocalDataSource.deleteAll()
    }
}