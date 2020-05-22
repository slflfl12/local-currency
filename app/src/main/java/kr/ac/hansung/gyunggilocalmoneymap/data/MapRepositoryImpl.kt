package kr.ac.hansung.gyunggilocalmoneymap.data

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
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

    override fun getPlaces(pIndex: String): Single<List<SHPlace>> =
        mapRemoteDataSource.getPlaces(pIndex)

    override fun saveAll(): Completable {
        return Observable.fromIterable(1..570).concatMap {
                mapRemoteDataSource.getPlaces(it.toString()).toObservable()
            }.concatMapCompletable {
                mapLocalDataSource.insertMaps(it)
        }.doAfterTerminate {
            mapLocalDataSource.appVersion = BuildConfig.VERSION_NAME
            Log.d("ddddddddddd", "dddddddddd")
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

    override fun deleteAll(): Completable {
        return mapLocalDataSource.deleteAll()
    }
}