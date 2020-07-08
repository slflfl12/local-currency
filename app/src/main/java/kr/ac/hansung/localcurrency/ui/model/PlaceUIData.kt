package kr.ac.hansung.localcurrency.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceUIData(
    val title: String = "",
    val roadAddress: String = "",
    val telePhone: String = "",
    val category: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distance: String = "",
    val distanceDouble: Double = 0.0
) : Parcelable