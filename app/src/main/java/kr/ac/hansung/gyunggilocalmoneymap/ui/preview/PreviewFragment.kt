package kr.ac.hansung.gyunggilocalmoneymap.ui.preview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.annotations.SerializedName
import com.naver.maps.geometry.LatLng
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.databinding.FragmentPreviewBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseBottomSheetDialogFragment
import kr.ac.hansung.gyunggilocalmoneymap.ui.preview.model.PreviewUIData
import kr.ac.hansung.gyunggilocalmoneymap.util.toDistanceString
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreviewFragment : BaseBottomSheetDialogFragment<FragmentPreviewBinding, PreviewViewModel>(R.layout.fragment_preview){

    override val vm: PreviewViewModel by viewModel()

    private lateinit var previewUIData: PreviewUIData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = arguments?.getParcelable<SHPlace>(KEY_MARKER_PROPERTY)

        val location = arguments?.getDoubleArray(KEY_MY_LOCATION)


        data?.let {
            val from = location?.let { LatLng(it[0], it[1])}
            val to = LatLng(it.latitude, it.longitude)

            previewUIData = PreviewUIData(
                title = it.title ?: "",
                roadAddress = it.roadAddress ?: "",
                telePhone = it.telePhone ?: "",
                category = it.category ?: "",
                distance = to.toDistanceString(from)
            )
        }


        Log.d("data", previewUIData.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.data = previewUIData
    }



    companion object {
        val TAG: String = this::class.java.simpleName

        const val KEY_MARKER_PROPERTY = "KEY_MARKER_PROPERTY"
        const val KEY_MY_LOCATION = "KEY_MY_LOCATION"

        fun newInstance(markerProperty: SHPlace, myLocationArray: DoubleArray) = PreviewFragment().apply {
            arguments =Bundle().apply {
                putParcelable(KEY_MARKER_PROPERTY, markerProperty)
                putDoubleArray(KEY_MY_LOCATION, myLocationArray)
            }
        }
    }


}