package kr.ac.hansung.gyunggilocalmoneymap.data.model

import com.google.gson.annotations.SerializedName

data class LocalMapResponse(
    @SerializedName("RegionMnyFacltStus")
    val regionMnyFacltStus: List<RegionMnyFacltStu>
) {
    data class RegionMnyFacltStu(
        val head: List<Head>,
        val row: List<Row>
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

        data class Row(
            val BIZCOND_NM: String?,
            val BRNHSTRM_MNY_USE_POSBL_YN: String?,
            val CARD_MNY_USE_POSBL_YN: String?,
            val CMPNM_NM: String?,
            val DATA_STD_DE: String?,
            val INDUTYPE_CD: String?,
            val INDUTYPE_NM: String?,
            val MOBILE_MNY_USE_POSBL_YN: String?,
            val REFINE_LOTNO_ADDR: String?,
            val REFINE_ROADNM_ADDR: String?,
            val REFINE_WGS84_LAT: String?,
            val REFINE_WGS84_LOGT: String?,
            val REFINE_ZIP_CD: String?,
            val REGION_MNY_NM: String?,
            val SIGUN_CD: String?,
            val SIGUN_NM: String?,
            val TELNO: String?
        )
    }
}
