package kr.ac.hansung.gyunggilocalmoneymap.data.local.mapper

import kr.ac.hansung.gyunggilocalmoneymap.data.local.model.MapEntity
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace

object MapEntityMapper {

    fun mapToLocal(from: SHPlace): MapEntity = MapEntity(
        id = 0L,
        title = from.title,
        telePhone = from.telePhone,
        roadAddress = from.roadAddress,
        latitude = from.latitude.toString(),
        longitude = from.longitude.toString(),
        sigun = from.sigun,
        category = from.category
    )

    fun mapToSHPlace(from: MapEntity): SHPlace = SHPlace(
        title = from.title,
        roadAddress = from.roadAddress,
        latitude = from.latitude?.toDouble()!!,
        longitude = from.longitude?.toDouble()!!,
        telePhone = from.telePhone,
        sigun = from.sigun,
        category = from.category
    )



}