package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Completable
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse.RegionMnyFacltStu.Place

interface FirebaseRepository {

    fun savePlaces(list: List<Place>) : Completable
}