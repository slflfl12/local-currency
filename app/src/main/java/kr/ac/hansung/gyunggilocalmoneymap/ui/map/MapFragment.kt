package kr.ac.hansung.gyunggilocalmoneymap.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_map.*
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.databinding.FragmentMapBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseFragment
import kr.ac.hansung.gyunggilocalmoneymap.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(R.layout.fragment_map),
    OnMapReadyCallback {


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

    private lateinit var sigunStringArray: List<String>
    private lateinit var sigunSpinner: Spinner
    private lateinit var sigunSpinnerAdapter: ArrayAdapter<CharSequence>

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
                vm.onChangedLocation(LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude))
            }



        }

        markerManager = MarkerManager(context!!, naverMap)




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

    }

    private fun initObserve() {


        vm.places.observe(this, Observer {
            if(it.isEmpty()) {
                markerManager.setMarkers(arrayListOf())

            }else {
                markerManager.setMarkers(ArrayList(it))
                CameraUpdate.fitBounds(markerManager.makeBounds()).animate(CameraAnimation.Fly).run {
                    naverMap.moveCamera(this)
                }
            }
        })


        vm.errorMessage.observe(this, Observer {
            showToast(requireContext(), it.toString())
        })

        vm.sigunSpinnerItem.observe(this, Observer {
            it?.let {
                vm.getPlacesBySigun(it)
                /*val position = sigunSpinnerAdapter.getPosition(it)
                sigunSpinner.setSelection(position)*/
            }
        })


    }

    private fun bindViewModel() {

        vm.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ pb_loading.isVisible = it}
            .addTo(compositeDisposable)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)


        val spinnerItem = menu.findItem(R.id.action_spinner)
        sigunSpinner = spinnerItem.actionView as Spinner

        sigunSpinnerAdapter = ArrayAdapter.createFromResource(context!!,R.array.sigun,
            android.R.layout.simple_spinner_item
            ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            sigunSpinner.adapter = adapter
        }

        sigunSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            )
            {
                vm.setSigunItem(parent?.getItemAtPosition(position).toString())
                Log.d("sh sigunSelected", parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val searchItem = menu.findItem(R.id.action_search).actionView

    }

    private fun animateFab() {

        if(isFabOpen) {
            binding.fab.startAnimation(rotateForward)
            binding.fab1.apply {
                startAnimation(fabClose)
                visibility = View.INVISIBLE
            }
            binding.fab2.apply {
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
            binding.fab2.apply {
                startAnimation(fabOpen)
                visibility = View.VISIBLE
            }
            isFabOpen = true;
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
        bindViewModel()
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
        compositeDisposable.clear()
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