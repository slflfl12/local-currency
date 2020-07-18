package kr.ac.hansung.localcurrency.data.remote.network

import io.reactivex.Single
import kr.ac.hansung.localcurrency.data.remote.model.GeocodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReverseGeoCodeService {


    @GET("gc?request=coordsToaddr&sourcecrs=epsg:4326&output=json")
    fun getGeocode(@Query("coords") coords: String): Single<GeocodeResponse>
}