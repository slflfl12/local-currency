package kr.ac.hansung.localcurrency.data.local.mapper

import kr.ac.hansung.localcurrency.data.local.model.MapEntity
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace

object MapEntityMapper {

    fun mapToLocal(from: SHPlace): MapEntity = MapEntity(
            id = 0L,
            title = from.title,
            telePhone = from.telePhone,
            roadAddress = from.roadAddress,
            latitude = from.latitude.toString(),
            latitudeDouble = from.latitude,
            longitudeDouble = from.longitude,
            longitude = from.longitude.toString(),
            sigun = from.sigun,
            category = from.category
    )

    fun mapToSHPlace(from: MapEntity): SHPlace = SHPlace(
            title = from.title,
            roadAddress = from.roadAddress,
            latitude = from.latitudeDouble!!,
            longitude = from.longitudeDouble!!,
            telePhone = from.telePhone,
            sigun = from.sigun,
            category = from.category
    )


}