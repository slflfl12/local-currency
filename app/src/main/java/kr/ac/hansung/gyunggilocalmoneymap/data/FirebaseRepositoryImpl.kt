package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Completable
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse.RegionMnyFacltStu.Place
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.FirebaseDataSource

class FirebaseRepositoryImpl(
    private val firebaseDataSource: FirebaseDataSource
) : FirebaseRepository {


    override fun savePlaces(list: List<Place>): Completable = firebaseDataSource.savePlaces(list)

}