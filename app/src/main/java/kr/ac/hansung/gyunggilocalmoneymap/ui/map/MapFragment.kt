package kr.ac.hansung.gyunggilocalmoneymap.ui.map

import android.os.Bundle
import android.util.Log
import android.view.View
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.databinding.FragmentMapBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(R.layout.fragment_map),
    OnMapReadyCallback {


    override val vm: MapViewModel by viewModel()


    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private var locationState: LocationTrackingMode = LocationTrackingMode.Follow


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@MapFragment)
        }

    }


    override fun onMapReady(map: NaverMap) {
        naverMap = map
        map.apply {
            locationSource = this@MapFragment.locationSource
            locationTrackingMode = locationState
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    companion object {

        val TAG = this::class.java.simpleName

        fun newInstance() = MapFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val SEARCH_REQUEST_CODE = 1
    }

}