package kr.ac.hansung.localcurrency.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_map.*
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import kr.ac.hansung.localcurrency.databinding.FragmentMapBinding
import kr.ac.hansung.localcurrency.ui.base.BaseFragment
import kr.ac.hansung.localcurrency.ui.detail.DetailActivity
import kr.ac.hansung.localcurrency.ui.map.cluster.ClusterDialog
import kr.ac.hansung.localcurrency.ui.map.preview.PreviewFragment
import kr.ac.hansung.localcurrency.ui.map.result.ResultAdapter
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.ui.search.SearchActivity
import kr.ac.hansung.localcurrency.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(R.layout.fragment_map),
        OnMapReadyCallback, MarkerManager.OnMarkerClickListener {


    override val vm: MapViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()


    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private var locationState: LocationTrackingMode = LocationTrackingMode.Follow
    private lateinit var markerManager: MarkerManager


    private var isFabOpen = false
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation

    private val searchClickSubject = PublishSubject.create<Unit>()


    //BottomSheet
    private val resultAdapter: ResultAdapter by lazy {
        ResultAdapter(vm)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@MapFragment)
        }
        initView()
        initObserve()

    }


    override fun onMapReady(map: NaverMap) {
        naverMap = map

        map.uiSettings.apply {
            isCompassEnabled = false
            isScaleBarEnabled = false
        }
        map.apply {
            locationSource = this@MapFragment.locationSource
            locationTrackingMode = locationState
            isNightModeEnabled = true
            mapType = NaverMap.MapType.Navi
            setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, false)

            addOnLocationChangeListener { location ->
                vm.onChangedMyLocation(location)
            }
            addOnCameraIdleListener {
                vm.onChangedLocation(
                        LatLng(
                                cameraPosition.target.latitude,
                                cameraPosition.target.longitude
                        )
                )
            }
        }
        markerManager = MarkerManager(context!!, naverMap).apply {
            onMarkerClickListener = this@MapFragment
        }


    }


    private fun initView() {


        binding.rvResult.adapter = resultAdapter

        setHasOptionsMenu(true)

        fabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(context, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(context, R.anim.rotate_backward)


        (activity as AppCompatActivity).setSupportActionBar(binding.tbMap)
        (activity as AppCompatActivity).supportActionBar?.run {
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }


        //BottomSheet

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        binding.tvResult.setOnClickListener {
            binding.bottomSheet.let {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.ivClear.setOnClickListener {
            binding.ivClear.setOnClickListener {
                binding.bottomSheet.let {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }


    }

    private fun initObserve() {


        vm.places.observe(this, Observer {

            it?.let {
                if (it.isEmpty()) {
                    markerManager.setMarkers(arrayListOf())
                } else {
                    markerManager.setMarkers(ArrayList(it))
                    CameraUpdate.fitBounds(markerManager.makeBounds()).animate(CameraAnimation.Fly)
                            .run {
                                naverMap.moveCamera(this)
                            }
                }

                markerManager.getMarkers().map {
                    val to = LatLng(it.latitude, it.longitude)
                    PlaceUIData(
                            title = it.title ?: "",
                            roadAddress = it.roadAddress ?: "",
                            telePhone = it.telePhone ?: "",
                            category = it.category ?: "",
                            latitude = it.latitude,
                            longitude = it.longitude,
                            distance = to.toDistanceString(vm.currentMyLocation.value),
                            distanceDouble = to.toDistance(vm.currentMyLocation.value)
                    )
                }

            }?.run {
                sortedBy { it.distanceDouble }
            }?.run {
                if (::markerManager.isInitialized) {
                    resultAdapter.submitList(this)
                }
            }

            markerManager.getMarkerSize().let { size ->

                binding.tvTotal.text = "총 ${size}개"

                if (size == 0) {
                    binding.tvRequestSearch.visible()
                    binding.rvResult.gone()
                } else {
                    binding.tvRequestSearch.gone()
                    binding.tvResult.visible()
                }
            }


        })

        vm.distanceValue.observe(this, Observer {
            showToast(requireContext(), String.format(getString(R.string.set_distance_text), it))
        })

        vm.showBottomSheetDialog.observe(this, Observer {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })

        vm.closeEvent.observe(this, Observer {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        })


        vm.errorMessage.observe(this, Observer {
            showToast(requireContext(), it.toString())
        })

        vm.itemClickEvent.observe(
                this@MapFragment, EventObserver(
                this@MapFragment::onItemClick
        )
        )

        vm.navigateToCallEvent.observe(
                this@MapFragment, EventObserver(
                this@MapFragment::onNavigateToCall
        )
        )

        vm.navigateToFindLoadEvent.observe(
                this@MapFragment, EventObserver(
                this@MapFragment::onNavigateFindLoad
        )
        )

        vm.fabClickEvent.observe(viewLifecycleOwner, Observer {
            animateFab()
        })

        vm.fab1ClickEvent.observe(viewLifecycleOwner, Observer {
            animateFab()
        })

        vm.fab2ClickEvent.observe(viewLifecycleOwner, Observer {
            animateFab()
        })


    }

    private fun bindViewModel() {

        vm.loadingSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    pb_loading.isVisible = it
                    tv_refresh.isVisible = !it
                    tv_result.isVisible = !it
                }
                .addTo(compositeDisposable)

        searchClickSubject.throttleFirst(2L, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    vm.currentMyLocation.value?.let {
                        Intent(context, SearchActivity::class.java).apply {
                            putExtra(
                                    SearchActivity.KEY_LOCATION,
                                    doubleArrayOf(it.latitude, it.longitude)
                            )
                        }
                                .also {
                                    startActivity(it)
                                }
                    }
                }.addTo(compositeDisposable)

    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_search -> {
                searchClickSubject.onNext(Unit)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun animateFab() {

        if (isFabOpen) {
            binding.fab.startAnimation(rotateForward)
            binding.fab1.apply {
                startAnimation(fabClose)
                visibility = View.INVISIBLE
            }
            binding.tvFabText1.apply {
                startAnimation(fabClose)
                visibility = View.INVISIBLE
            }
            binding.fab2.apply {
                startAnimation(fabClose)
                visibility = View.INVISIBLE
            }
            binding.tvFabText2.apply {
                startAnimation(fabClose)
                visibility = View.INVISIBLE
            }
            isFabOpen = false;
        } else {
            binding.fab.startAnimation(rotateBackward)
            binding.fab1.apply {
                startAnimation(fabOpen)
                visibility = View.VISIBLE
            }
            binding.tvFabText1.apply {
                startAnimation(fabOpen)
                visibility = View.VISIBLE
            }
            binding.fab2.apply {
                startAnimation(fabOpen)
                visibility = View.VISIBLE
            }
            binding.tvFabText2.apply {
                startAnimation(fabOpen)
                visibility = View.VISIBLE
            }
            isFabOpen = true;
        }
    }

    override fun onMarkerClick(markerProperty: SHPlace) {
        vm.currentMyLocation.value?.let {
            PreviewFragment.newInstance(markerProperty, doubleArrayOf(it.latitude, it.longitude))
                    .apply {
                    }.show(childFragmentManager, PreviewFragment.TAG)
        }
    }

    override fun onClusterClick(markers: Collection<SHPlace>) {
        vm.currentLocation.value?.let {
            ClusterDialog.newInstance(
                    markers as ArrayList<SHPlace>,
                    doubleArrayOf(it.latitude, it.longitude)
            )
                    .show(childFragmentManager, ClusterDialog.TAG)
        }

    }

    private fun onItemClick(placeUIData: PlaceUIData) {
        Intent(context, DetailActivity::class.java).apply {
            vm.currentMyLocation.value?.let {
                putExtra(DetailActivity.KEY_LOCATION, doubleArrayOf(it.latitude, it.longitude))
                putExtra(DetailActivity.KEY_PLACE, placeUIData)
            }.also {
                startActivity(it)
            }
        }
    }

    private fun onNavigateToCall(placeUIData: PlaceUIData) {
        startActivity(
                Intent(
                        Intent.ACTION_DIAL,
                        ("tel:${placeUIData.telePhone.splitPhoneNum()}").toUri()
                )
        )
    }

    private fun onNavigateFindLoad(placeUIData: PlaceUIData) {
        val url =
                "nmap://route/walk?dlat=${placeUIData.latitude}&dlng=${placeUIData.longitude}&dname=${placeUIData.title}&appname=kr.ac.hansung.localcurrency"
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        val list: List<ResolveInfo> = context?.packageManager?.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
        ) as List<ResolveInfo>

        if (list.isEmpty()) {
            startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.nhn.android.nmap")
                    )
            )
        } else {
            startActivity(intent)
        }
    }

    fun getBottomSheetState(): Int = bottomSheetBehavior.state

    fun closeBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
        bindViewModel()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()

    }

    override fun onPause() {
        binding.mapView.onResume()
        compositeDisposable.clear()
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

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        //여기에 clear가 있을 경우
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    companion object {

        val TAG = this::class.java.simpleName

        fun newInstance() = MapFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

}