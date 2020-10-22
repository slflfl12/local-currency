package kr.ac.hansung.localcurrency.util

import android.util.Log
import com.naver.maps.geometry.LatLng
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import java.text.FieldPosition

const val REFERANCE_LAT_X1 = 1 / 109.958489129649955
const val REFERANCE_LNG_X1 = 1 / 88.74
const val REFERANCE_LAT_X3 = 3 / 109.958489129649955
const val REFERANCE_LNG_X3 = 3 / 88.74

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

fun withinSightMarker_X3(currentPosition: LatLng, markerPosition: LatLng): Boolean {
    val withinSightMarkerLat = Math.abs(currentPosition.latitude - markerPosition.latitude) <= REFERANCE_LAT_X3
    val withinSightMarkerLng = Math.abs(currentPosition.longitude - markerPosition.longitude) <= REFERANCE_LNG_X3

    return withinSightMarkerLat && withinSightMarkerLng
}

fun withinSightMarker_X1(currentPosition: LatLng, markerPosition: LatLng): Boolean {
    val withinSightMarkerLat = Math.abs(currentPosition.latitude - markerPosition.latitude) <= REFERANCE_LAT_X1
    val withinSightMarkerLng = Math.abs(currentPosition.longitude - markerPosition.longitude) <= REFERANCE_LNG_X1

    return withinSightMarkerLat && withinSightMarkerLng
}

fun SHPlace.withinSightMarker(currentPosition: LatLng): Boolean {
    val withinSightMarkerLat = Math.abs(currentPosition.latitude - this.latitude) <= REFERANCE_LAT_X1
    val withinSightMarkerLng = Math.abs(currentPosition.longitude - this.longitude) <= REFERANCE_LNG_X1

    return withinSightMarkerLat && withinSightMarkerLng
}
