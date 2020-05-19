package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Completable
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse.RegionMnyFacltStu.Place

interface FirebaseRepository {

    fun savePlaces(list: List<Place>) : Completable
}