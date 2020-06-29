package kr.ac.hansung.gyunggilocalmoneymap.ui.map.result

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.databinding.FragmentResultBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseBottomSheetDialogFragment
import kr.ac.hansung.gyunggilocalmoneymap.ui.map.MapViewModel
import kr.ac.hansung.gyunggilocalmoneymap.ui.model.PlaceUIData
import kr.ac.hansung.gyunggilocalmoneymap.util.showToast
import kr.ac.hansung.gyunggilocalmoneymap.util.toDistanceString
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ResultFragment : BaseBottomSheetDialogFragment<FragmentResultBinding, MapViewModel>(R.layout.fragment_result), ResultAdapter.ItemClickListener{

    override val vm: MapViewModel by sharedViewModel()

    private val resultAdapter: ResultAdapter by lazy {
        ResultAdapter().apply {
            itemClickListener = this@ResultFragment
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvResult.apply {
            adapter = resultAdapter
        }
        initView()
        initObserve()
    }

    private fun initView() {
        binding.ivClear.setOnClickListener {
            showToast(requireContext(), "ivclear")
            binding.root.let {
                BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

    }


    private fun initObserve() {

        vm.places.observe(this, Observer {
            showToast(requireContext(), "observing")
            Log.d("result", "result")
            it?.let {

                it.map {
                val to = LatLng(it.latitude, it.longitude)
                PlaceUIData(
                    title = it.title ?: "",
                    roadAddress = it.roadAddress ?: "",
                    telePhone = it.telePhone ?: "",
                    category = it.category ?: "",
                    distance = to.toDistanceString(vm.currentMyLocation.value)
                )
            }
        }?.run {
            resultAdapter.submitList(this)
        }
        })

    }

    override fun itemClick(placeUiData: PlaceUIData) {

    }



    companion object {
        val TAG: String = this::class.java.simpleName


        fun newInstance() = ResultFragment()

    }

}