package kr.ac.hansung.gyunggilocalmoneymap.data

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.data.local.mapper.MapEntityMapper
import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.MapLocalDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.MapRemoteDataSource

class MapRepositoryImpl(
    private val mapLocalDataSource: MapLocalDataSource,
    private val mapRemoteDataSource: MapRemoteDataSource
) : MapRepository {

    override val appVersion: String?
        get() = mapLocalDataSource.appVersion

    override val pageLoadingSubject: BehaviorSubject<Float>
        get() = mapRemoteDataSource.pageLoadingSubject

    override fun getPlaces(pIndex: String): Single<List<SHPlace>> =
        mapRemoteDataSource.getPlaces(pIndex)

    override fun saveAll(): Completable {
        return mapLocalDataSource.deleteAll()
            .flatMap {
                Observable.fromIterable(1..570)
                    .concatMapSingle { page ->
                        pageLoadingSubject.onNext(page / 570f)
                        mapRemoteDataSource.getPlaces(page.toString())
                    }.concatMapCompletable {
                        mapLocalDataSource.insertMaps(it)
                    }
            }.doOnComplete {
                mapLocalDataSource.appVersion = BuildConfig.VERSION_NAME
                Log.d("sh caching", "sh caching")
            }


/*        return mapRemoteDataSource.saveAll()
            .concatMap {
                mapRemoteDataSource.getPlaces(it.toString()).toObservable()
            }
            .concatMapCompletable {
                mapLocalDataSource.insertMaps(it.regionMnyFacltStus[1].places)
            }
            .doAfterTerminate {
                mapLocalDataSource.appVersion = BuildConfig.VERSION_NAME
            }*/

    }

    override fun getMapEntities(): Single<List<SHPlace>> {
        return mapLocalDataSource.getMapEntities()
            .map { it.map(MapEntityMapper::mapToSHPlace) }
            .doOnSubscribe {
                Log.d("sh 처리중", "sh 처리중")
            }
    }

    override fun deleteAll(): Completable {
        return mapLocalDataSource.deleteAll()
    }
}