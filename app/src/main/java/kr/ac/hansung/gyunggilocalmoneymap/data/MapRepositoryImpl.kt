package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Completable
import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.MapLocalDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.MapRemoteDataSource

class MapRepositoryImpl(
    private val mapLocalDataSource: MapLocalDataSource,
    private val mapRemoteDataSource: MapRemoteDataSource
) : MapRepository {



    override fun getPlaces(pIndex: String): Single<LocalMapResponse> =
        mapRemoteDataSource.getPlaces(pIndex)

    override fun saveAll(): Completable {
        return mapRemoteDataSource.saveAll()
            .concatMap {
                mapRemoteDataSource.getPlaces(it.toString()).toObservable()
            }
            .concatMapCompletable {
                mapLocalDataSource.insertMaps(it.regionMnyFacltStus[1].places)
            }
    }

    override fun deleteAll(): Completable {
        return mapLocalDataSource.deleteAll()
    }
}