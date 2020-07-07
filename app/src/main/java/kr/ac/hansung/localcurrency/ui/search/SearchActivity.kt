package kr.ac.hansung.localcurrency.ui.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.pb_loading
import kotlinx.android.synthetic.main.fragment_map.*
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.remote.model.OpenApiMapResponse
import kr.ac.hansung.localcurrency.databinding.ActivitySearchBinding
import kr.ac.hansung.localcurrency.ui.base.BaseActivity
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.util.toDistance
import kr.ac.hansung.localcurrency.util.toDistanceString
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity :
    BaseActivity<ActivitySearchBinding, SearchViewModel>(R.layout.activity_search), SearchAdapter.ItemClickListener {

    override val vm: SearchViewModel by viewModel()

    private lateinit var searchAdapter: SearchAdapter

    private var currentLocation: LatLng? = null

    private val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.extras?.getDoubleArray(KEY_LOCATION)
        data?.let {
            currentLocation = LatLng(it[0], it[1]).also {
                vm.setCurrentLocation(it)
            }
        }
        initView()
        initObserve()
    }

    private fun initView() {
        searchAdapter = SearchAdapter()
        binding.rvSearch.adapter = searchAdapter



        binding.etSearch.apply {

            setOnKeyListener { v, keyCode, event ->
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    vm.searchPlaces()
                    return@setOnKeyListener true
                }
                else if(keyCode == KeyEvent.KEYCODE_BACK) {
                    clearFocus()
                    return@setOnKeyListener true
                }

                return@setOnKeyListener false

            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

    }

    private fun initObserve() {

        vm.places.observe(this, Observer {
            it?.let {

                it.map {
                    currentLocation?.let { currentLocation ->
                        PlaceUIData(
                            title = it.title ?: "",
                            roadAddress = it.roadAddress ?: "",
                            telePhone = it.telePhone ?: "",
                            category = it.category ?: "",
                            distance = currentLocation.toDistanceString(LatLng(it.latitude, it.longitude)),
                            distanceDouble = currentLocation.toDistance(LatLng(it.latitude, it.longitude))
                        )
                    }
                }
            }?.run {
                sortedBy { it?.distanceDouble }
            }?.run {
                if(::searchAdapter.isInitialized)
                    searchAdapter.submitList(this)
                binding.tvTotal.text = String.format(getString(R.string.tv_total_text), it.size)
            }


            binding.tvEmpty.visibility = View.INVISIBLE
            binding.rvSearch.visibility = View.VISIBLE
        })

        vm.errorResultEmpty.observe(this, Observer {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.rvSearch.visibility = View.INVISIBLE
        })


        vm.errorThrowable.observe(this, Observer {
            showToast(it.toString())
        })

        vm.errorQueryEmpty.observe(this, Observer {
            showToast("빈칸을 채워주세요")
        })

        vm.isProgressBoolean.observe(this, Observer {
            if(it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        })

        vm.isKeyBoardBoolean.observe(this, Observer {
            if(!it) {
                hideKeyboard()
            }
        })
    }

    override fun itemClick(placeUIData: PlaceUIData) {
        showToast("itemClick")
    }

    override fun callClick() {
        showToast("callClick")
    }

    override fun findLoad() {
        showToast("findLoadClick")
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }






    companion object {
        const val KEY_LOCATION = "key_location"
    }

}