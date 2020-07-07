package kr.ac.hansung.localcurrency.data.remote.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
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
) : TedClusterItem, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun getTedLatLng(): TedLatLng {
        return TedLatLng(latitude, longitude)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(roadAddress)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(telePhone)
        parcel.writeString(sigun)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<SHPlace> {
        override fun createFromParcel(parcel: Parcel): SHPlace {
            return SHPlace(parcel)
        }

        override fun newArray(size: Int): Array<SHPlace?> {
            return arrayOfNulls(size)
        }
    }
}