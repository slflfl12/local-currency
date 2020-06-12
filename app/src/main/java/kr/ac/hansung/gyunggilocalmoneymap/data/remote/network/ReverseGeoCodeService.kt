package kr.ac.hansung.gyunggilocalmoneymap.data.remote.network

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.GeocodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReverseGeoCodeService {


    @GET("?request=coordsToaddr&sourcecrs=epsg:4326&output=json&orders=roadaddr")
    fun getGeocode(@Query("coords") coords: String): Single<GeocodeResponse>
}