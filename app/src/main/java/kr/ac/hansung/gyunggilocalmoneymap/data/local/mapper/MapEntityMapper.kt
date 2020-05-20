package kr.ac.hansung.gyunggilocalmoneymap.data.local.mapper

import kr.ac.hansung.gyunggilocalmoneymap.data.local.model.MapEntity
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse.RegionMnyFacltStu.Place

object MapEntityMapper {

    fun mapToLocal(from: Place): MapEntity = MapEntity(
        id = 0L,
        title = from.title,
        telePhone = from.telephone,
        roadAddress = from.roadAddress,
        latitude = from.latitude,
        longitude = from.longitude
    )



}