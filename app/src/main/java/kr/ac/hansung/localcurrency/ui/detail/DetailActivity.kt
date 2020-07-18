package kr.ac.hansung.localcurrency.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import kr.ac.hansung.localcurrency.databinding.ActivityDetailBinding
import kr.ac.hansung.localcurrency.ui.base.BaseActivity
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.util.splitPhoneNum
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail), OnMapReadyCallback {

    override val vm: DetailViewModel by viewModel()


    private lateinit var naverMap: NaverMap

    private var currentLocation: LatLng? = null
    private var uiData: PlaceUIData? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        initView()

        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@DetailActivity)
        }

        val location = intent?.extras?.getDoubleArray(KEY_LOCATION)
        location?.let {
            currentLocation = LatLng(it[0], it[1])
        }

        val place = intent?.extras?.getParcelable<PlaceUIData>(KEY_PLACE)
        place?.let {
            uiData = it
            binding.uiData = it
        }


        initObserve()
    }

    private fun initView() {

        binding.ivBack.setOnClickListener {
            finish()
        }

    }

    private fun initObserve() {

        vm.navigateToCallEvent.observe(this,
                Observer { onNavigateToCall() })

        vm.navigateToFindLoadEvent.observe(this,
                Observer { onNavigateFindLoad() })
    }

    private fun onNavigateFindLoad() {
        uiData?.let {
            val url = "nmap://route/walk?dlat=${it.latitude}&dlng=${it.longitude}&dname=${it.title}&appname=kr.ac.hansung.localcurrency"
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.addCategory(Intent.CATEGORY_BROWSABLE)

            val list: List<ResolveInfo> = getPackageManager()?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY) as List<ResolveInfo>

            if (list.isEmpty()) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.nmap")))
            } else {
                startActivity(intent)
            }
        }
    }

    private fun onNavigateToCall() {
        startActivity(Intent(Intent.ACTION_DIAL, ("tel:${uiData?.telePhone?.splitPhoneNum()}").toUri()))
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        map.uiSettings.apply {
            isRotateGesturesEnabled = false // 각도 바뀌는거 해제
            isZoomControlEnabled = false // 더블클릭하면 줌 되는거 해제
            isScrollGesturesEnabled = false // 지도를 드래그하면 따라가는 것 해제
            isCompassEnabled = false // 나침반 해제
            isLocationButtonEnabled = false // 버튼 없애기
            isTiltGesturesEnabled = false // 회전되지 않은 각도만 보여주게 하기 위해서

        }

        uiData?.let {
            if (::naverMap.isInitialized) {
                makeMarker(it)
            }
        }

    }

    private fun makeMarker(uiData: PlaceUIData) {

        val marker = Marker().apply {
            position = LatLng(uiData.latitude, uiData.longitude)
            map = naverMap
        }

        InfoWindow().apply {
            adapter = MarkerInfoWindowAdapter(uiData)
            offsetY = -90
        }.open(marker, Align.Bottom)

        CameraUpdate.scrollTo(marker.position).let {
            naverMap.moveCamera(it)
        }

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
        binding.mapView.onResume()
        super.onPause()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    inner class MarkerInfoWindowAdapter(private val uiData: PlaceUIData) : InfoWindow.ViewAdapter() {

        private val rootView = LayoutInflater.from(this@DetailActivity).inflate(R.layout.item_marker_name, null)
        private val name = rootView.findViewById<TextView>(R.id.tv_marker_name)

        override fun getView(infoWindow: InfoWindow): View {
            name.text = uiData.title
            return rootView
        }

    }

    companion object {
        val TAG = this::class.java.simpleName

        val KEY_LOCATION = "KEY_LOCATION"
        val KEY_PLACE = "KEY_PLACE"
    }

}