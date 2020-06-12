package kr.ac.hansung.gyunggilocalmoneymap.ui.map

import android.content.Context
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import ted.gun0912.clustering.naver.TedNaverClustering

class MarkerManager(val context: Context, private val naverMap: NaverMap) {

    private val places = HashMap<Marker, SHPlace>()
    private val markers = HashMap<SHPlace, Marker>()

    private val cluster = TedNaverClustering.with<SHPlace>(context, naverMap)

    fun setMarkers(placeList: ArrayList<SHPlace>) {
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

    private fun addMarkers(placeList: ArrayList<SHPlace>) {
/*        for (place in placeList) {
            if (markers.containsKey(place).not()) {
                drawMarker(place).run {
                    markers[place] = this
                    places[this] = place
                    map = naverMap
                }
            }
        }*/

        cluster.items(placeList).customMarker {
            Marker().apply {
                captionText = it.title!!
            }
        }.make()

    }

    private fun drawMarker(place: SHPlace): Marker {
        place.let { place ->
            return Marker().apply {
                position = LatLng(place.latitude, place.longitude)
                captionText = place.title!!
                onClickListener = Overlay.OnClickListener {

                    true
                }
            }

        }

    }
}