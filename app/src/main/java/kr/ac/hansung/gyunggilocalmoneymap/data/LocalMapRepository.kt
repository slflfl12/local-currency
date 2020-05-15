package kr.ac.hansung.gyunggilocalmoneymap.data

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse

interface LocalMapRepository {

    fun getAllPlace() : Single<LocalMapResponse>
}