package kr.ac.hansung.gyunggilocalmoneymap.data.remote.model

import com.google.gson.annotations.SerializedName
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

data class SHPlace(
    @SerializedName("CMPNM_NM")
    val title: String?,
    @SerializedName("REFINE_ROADNM_ADDR")
    val roadAddress: String?,
    @SerializedName("REFINE_WGS84_LAT")
    val latitude: Double,
    @SerializedName("REFINE_WGS84_LOGT")
    val longitude: Double,
    @SerializedName("TELNO")
    val telePhone: String?,
    val sigun: String?,
    val category: String?
) : TedClusterItem {
    override fun getTedLatLng(): TedLatLng {
        return TedLatLng(latitude, longitude)
    }
}