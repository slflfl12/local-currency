package kr.ac.hansung.gyunggilocalmoneymap.data.local.mapper

import kr.ac.hansung.gyunggilocalmoneymap.data.local.model.MapEntity
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse.RegionMnyFacltStu.Place
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

object MapEntityMapper {

    fun mapToLocal(from: Place): MapEntity = MapEntity(
        id = 0L,
        title = from.title,
        telePhone = from.telePhone,
        roadAddress = from.roadAddress,
        latitude = from.latitude,
        longitude = from.longitude
    )

    fun mapToSHPlace(from: MapEntity): SHPlace = SHPlace(
        title = from.title,
        roadAddress = from.roadAddress,
        latitude = from.latitude!!.toDouble(),
        longitude = from.longitude!!.toDouble(),
        telePhone = from.telePhone
    )



}