package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import io.reactivex.Completable
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse.RegionMnyFacltStu.Place

interface FirebaseDataSource {

    fun savePlaces(list: List<Place>) : Completable
}