package kr.ac.hansung.localcurrency.ui.map

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import ted.gun0912.clustering.clustering.Cluster
import ted.gun0912.clustering.naver.TedNaverClustering
import java.util.stream.Collectors

class MarkerManager(private val context: Context, private val naverMap: NaverMap) {

    private val markerProperties = HashMap<Marker, SHPlace>()
    private val markers = HashMap<SHPlace, Marker>()
    private val cluster = TedNaverClustering.with<SHPlace>(context, naverMap)
        .customMarker(::makeMarker)
        .markerClickListener {
            onMarkerClickListener?.markerClick(it)
        }
        .clusterClickListener { 
            onMarkerClickListener?.clusterClick(it.items)
        }
        .make()

    var onMarkerClickListener: OnMarkerClickListener? = null


    fun setMarkers(markerProperties: ArrayList<SHPlace>) {
        removeMarkers(markerProperties)
        addMarkers(markerProperties)
        Log.d("sh setMarker", "sh, setMarker")
    }


    private fun removeMarkers(markerArray: ArrayList<SHPlace>) {

        val removeMarkerProperties = ArrayList<SHPlace>()
        for (markerProperty in markers.keys) { //현재 맵에 띄워진 마커들
            for (newMarker in markerArray) { // 추가하려는 마커들
                if (markerProperty.equals(newMarker).not()) {
                    removeMarkerProperties.add(markerProperty)
                }
            }
        }

        for (removeMarker in removeMarkerProperties) {
            markers[removeMarker]?.run {
                markers.remove(removeMarker)
                markerProperties.remove(this)
                cluster.removeItem(removeMarker)
            }
        }


    }

    private fun addMarkers(placeList: ArrayList<SHPlace>) {
        for (place in placeList) {
            if (markers.containsKey(place).not()) {
                makeMarker(place).run {
                    markers[place] = this
                    markerProperties[this] = place
                    cluster.addItem(place)
                }
            }
        }


    }

    fun makeBounds(): LatLngBounds {
        val bounds = LatLngBounds.Builder()
        markers.map { LatLng(it.key.longitude, it.key.latitude) }.forEach { bounds.include(it) }
        return bounds.build()
    }

    private fun makeMarker(place: SHPlace): Marker {
        place.let { place ->
            return Marker().apply {
                captionText = place.title!!
            }

        }

    }

    fun getMarkerProperty(marker: Marker) = markerProperties[marker]

    fun getMarker(markerProperty: SHPlace) = markers[markerProperty]

    fun getMarkers() = markers.keys

    fun getMarkerSize() = markers.size

    fun selectMarker(markerProperty: SHPlace) {
        val marker = markers[markerProperty]
    }

    fun deSelectMarker() {

    }

    interface OnMarkerClickListener {
        fun markerClick(markerProperty: SHPlace)
        fun clusterClick(markerProperties: Collection<SHPlace>)
    }

    inner class InfoWindowAdapter(private val markerProperty: SHPlace) : InfoWindow.ViewAdapter() {
        private val view = LayoutInflater.from(context).inflate(R.layout.item_marker_name, null)
        private val name = view.findViewById<TextView>(R.id.tv_marker_name)

        override fun getView(p0: InfoWindow): View {
            name.text = markerProperty.title
            return view
        }
    }
}