package kr.ac.hansung.gyunggilocalmoneymap.util

import android.util.Log
import com.naver.maps.geometry.LatLng

fun LatLng.toDistance(from: LatLng?): Double = from?.let { this.distanceTo(it) } ?: 0.0



fun LatLng.toDistanceString(from: LatLng?): String {

    var meter = from?.let { this.distanceTo(it) } ?: 0.0

    return when {
        meter > 1000 -> {
            meter /= 1000
            String.format("%.2f km", meter)
        }
        else -> String.format("%.2f m", meter)
    }
}

//lat,lng
fun LatLng.toForApiString(): String {


    return "$longitude,$latitude"
}