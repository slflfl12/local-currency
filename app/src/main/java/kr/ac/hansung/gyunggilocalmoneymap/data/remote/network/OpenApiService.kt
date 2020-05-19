package kr.ac.hansung.gyunggilocalmoneymap.data.remote.network

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface OpenApiService {


    @GET("RegionMnyFacltStus?type=json&KEY=7a7e544f0c624662a876055da2f91d3e&pSize=1000")
    fun getPlaces(@Query("pIndex") pIndex: String): Single<LocalMapResponse>
}