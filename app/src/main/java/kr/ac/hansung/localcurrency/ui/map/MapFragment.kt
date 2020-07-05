package kr.ac.hansung.localcurrency.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_map.*
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import kr.ac.hansung.localcurrency.databinding.FragmentMapBinding
import kr.ac.hansung.localcurrency.ui.base.BaseFragment
import kr.ac.hansung.localcurrency.ui.map.preview.PreviewFragment
import kr.ac.hansung.localcurrency.ui.map.result.ResultAdapter
import kr.ac.hansung.localcurrency.ui.map.result.ResultFragment
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.ui.search.SearchActivity
import kr.ac.hansung.localcurrency.util.showToast
import kr.ac.hansung.localcurrency.util.toDistance
import kr.ac.hansung.localcurrency.util.toDistanceString
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(R.layout.fragment_map),
    OnMapReadyCallback, MarkerManager.OnMarkerClickListener, ResultAdapter.ItemClickListener {


    override val vm: MapViewModel by sharedViewModel()
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

    private lateinit var sigunStringArray: List<String>
    private lateinit var sigunSpinner: Spinner
    private lateinit var sigunSpinnerAdapter: ArrayAdapter<CharSequence>

    private val searchClickSubject = PublishSubject.create<Unit>()

    //BottomSheet
    private val resultAdapter: ResultAdapter by lazy {
        ResultAdapter().apply {
            itemClickListener = this@MapFragment
        }
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


/*        vm.currentMyLocation.value?.let {
            CameraUpdate.scrollTo(it)
                .animate(CameraAnimation.Fly)
                .finishCallback {

                }.also {
                    map.moveCamera(it)
                }
        }*/


        //vm.getMapEntities()
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
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black)
            setDisplayShowTitleEnabled(false)
        }

        binding.fab.setOnClickListener {
            animateFab()
        }

        binding.fab1.setOnClickListener {
            vm.setNearByValue(1.5f)
        }

        binding.fab2.setOnClickListener {
            vm.setNearByValue(2f)
        }

        resources.getStringArray(R.array.sigun).asList().apply {
            sigunStringArray = this
            vm._sigunStringList.value = this
        }

        //BottomSheet

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        binding.tvResult.setOnClickListener {
            showToast(requireContext(), vm.allPlaces.value?.size.toString())
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
                        distance = to.toDistanceString(vm.currentMyLocation.value),
                        distanceDouble = to.toDistance(vm.currentMyLocation.value)
                    )

                }
            }?.run {
                sortedBy { it.distanceDouble }
            }?.run {
                resultAdapter.submitList(this)
            }


            binding.tvTotal.text = "총 ${markerManager.getMarkerSize()}개"


        })




        vm.errorMessage.observe(this, Observer
        {
            showToast(requireContext(), it.toString())
        })

        vm.sigunSpinnerItem.observe(this, Observer
        {
            it?.let {
                vm.reqPlacesBySigun(it)
                /*val position = sigunSpinnerAdapter.getPosition(it)
                sigunSpinner.setSelection(position)*/
            }
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
                vm.currentLocation.value?.let {
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


        val spinnerItem = menu.findItem(R.id.action_spinner)
        sigunSpinner = spinnerItem.actionView as Spinner

        sigunSpinnerAdapter = ArrayAdapter.createFromResource(
            context!!, R.array.sigun,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            sigunSpinner.adapter = adapter
        }

        sigunSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                vm.setSigunItem(parent?.getItemAtPosition(position).toString())
                Log.d("sh sigunSelected", parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


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

    override fun markerClick(markerProperty: SHPlace) {
        vm.currentMyLocation.value?.let {
            PreviewFragment.newInstance(markerProperty, doubleArrayOf(it.latitude, it.longitude))
                .apply {
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundBottomSheetDialog)
                }.show(childFragmentManager, PreviewFragment.TAG)

            Log.d("fragment", "fragment")
        }
    }

    override fun itemClick(placeUiData: PlaceUIData) {
        showToast(requireContext(), "클릭")
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
        private const val SEARCH_REQUEST_CODE = 1
    }

}