package kr.ac.hansung.localcurrency.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import kotlinx.android.synthetic.main.activity_search.*
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.extras?.getDoubleArray(KEY_LOCATION)
        data?.let {
            currentLocation = LatLng(it[0], it[1])
        }
        initView()
        initObserve()
    }

    private fun initView() {
        searchAdapter = SearchAdapter()
        binding.rvSearch.adapter = searchAdapter

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
                if(::searchAdapter.isInitialized)
                    searchAdapter.submitList(this)
                Log.d("aa", this.toString())
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


    companion object {

        const val KEY_LOCATION = "key_location"
    }

}