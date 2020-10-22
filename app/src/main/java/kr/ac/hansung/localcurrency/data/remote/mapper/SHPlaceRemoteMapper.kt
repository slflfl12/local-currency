package kr.ac.hansung.localcurrency.data.remote.mapper

import kr.ac.hansung.localcurrency.data.remote.model.OpenApiMapResponse.RegionMnyFacltStu.Place
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace

object SHPlaceRemoteMapper {

    fun mapToData(from: Place): SHPlace = SHPlace(
            title = from.title,
            roadAddress = from.roadAddress,
            latitude = from.latitude!!.toDouble(),
            longitude = from.longitude!!.toDouble(),
            telePhone = from.telePhone,
            sigun = from.sigun,
            category = from.category
    )


}