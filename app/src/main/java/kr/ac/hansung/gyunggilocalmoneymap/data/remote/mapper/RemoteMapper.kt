package kr.ac.hansung.gyunggilocalmoneymap.data.remote.mapper

import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse.RegionMnyFacltStu.Place
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

object RemoteMapper {

    fun mapToData(from: Place) : SHPlace = SHPlace(
        title = from.title,
        roadAddress = from.roadAddress,
        latitude = from.latitude!!.toDouble(),
        longitude = from.longitude!!.toDouble(),
        telePhone = from.telePhone
    )


}