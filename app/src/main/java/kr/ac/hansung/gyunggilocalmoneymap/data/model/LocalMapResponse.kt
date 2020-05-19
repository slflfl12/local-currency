package kr.ac.hansung.gyunggilocalmoneymap.data.model

import com.google.gson.annotations.SerializedName

data class LocalMapResponse(
    @SerializedName("RegionMnyFacltStus")
    val regionMnyFacltStus: List<RegionMnyFacltStu>
) {
    data class RegionMnyFacltStu(
        val head: List<Head>,
        @SerializedName("row")
        val places: List<Place>
    ) {
        data class Head(
            @SerializedName("RESULT")
            val result: RESULT,
            val api_version: String,
            val list_total_count: Int
        ) {
            data class RESULT(
                @SerializedName("CODE")
                val code: String,
                @SerializedName("MESSAGE")
                val message: String
            )
        }

        data class Place(
            val BIZCOND_NM: String?,
            val BRNHSTRM_MNY_USE_POSBL_YN: String?,
            val CARD_MNY_USE_POSBL_YN: String?,
            @SerializedName("CMPNM_NM")
            val title: String?,
            val DATA_STD_DE: String?,
            val INDUTYPE_CD: String?,
            val INDUTYPE_NM: String?,
            val MOBILE_MNY_USE_POSBL_YN: String?,
            val REFINE_LOTNO_ADDR: String?,
            @SerializedName("REFINE_ROADNM_ADDR")
            val roadAddress: String?,
            @SerializedName("REFINE_WGS84_LAT")
            val latitude: String?,
            @SerializedName("REFINE_WGS84_LOGT")
            val longitude: String?,
            val REFINE_ZIP_CD: String?,
            val REGION_MNY_NM: String?,
            val SIGUN_CD: String?,
            val SIGUN_NM: String?,
            @SerializedName("TELNO")
            val telephone: String?
        )
    }
}
