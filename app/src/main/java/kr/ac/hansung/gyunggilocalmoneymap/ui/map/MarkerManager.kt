package kr.ac.hansung.gyunggilocalmoneymap.ui.map

import android.content.Context
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse.RegionMnyFacltStu.Place

class MarkerManager(val context: Context, private val naverMap: NaverMap) {

    private val places = HashMap<Marker, Place>()
    private val markers = HashMap<Place, Marker>()


    fun setMarkers(placeList: ArrayList<Place>) {
        println("set")
        removeMarkers()
        addMarkers(placeList)

    }


    private fun removeMarkers() {
        val iterator = markers.iterator()
        while (iterator.hasNext()) {
            var hash = iterator.next()
            var place = hash.key
            markers[place]?.run {
                map = null
                places.remove(this)
                iterator.remove()
            }
        }
    }

    private fun addMarkers(placeList: ArrayList<Place>) {
        for (place in placeList) {
            if (markers.containsKey(place).not()) {
                drawMarker(place).run {
                    markers[place] = this
                    places[this] = place
                    map = naverMap
                }
            }
        }
    }

    private fun drawMarker(place: Place): Marker {
        place.let { place ->
            return Marker().apply {
                    position = LatLng(place.latitude!!.toDouble(), place.longitude!!.toDouble())
                    captionText = place.title!!
                    onClickListener = Overlay.OnClickListener {

                        true
                    }
                }

        }

    }
}