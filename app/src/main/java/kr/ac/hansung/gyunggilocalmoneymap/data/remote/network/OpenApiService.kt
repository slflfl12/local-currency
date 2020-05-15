package kr.ac.hansung.gyunggilocalmoneymap.data.remote.network

import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface OpenApiService {


    @GET("RegionMnyFacltStus?type=json&KEY=7a7e544f0c624662a876055da2f91d3e")
    fun getAllPlaces(): Single<LocalMapResponse>
}