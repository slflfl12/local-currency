package kr.ac.hansung.localcurrency.ui.map.preview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import com.naver.maps.geometry.LatLng
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import kr.ac.hansung.localcurrency.databinding.FragmentPreviewBinding
import kr.ac.hansung.localcurrency.ui.base.BaseBottomSheetDialogFragment
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.util.toDistance
import kr.ac.hansung.localcurrency.util.toDistanceString
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreviewFragment :
    BaseBottomSheetDialogFragment<FragmentPreviewBinding, PreviewViewModel>(R.layout.fragment_preview) {

    override val vm: PreviewViewModel by viewModel()

    private lateinit var placeUIData: PlaceUIData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = arguments?.getParcelable<SHPlace>(KEY_MARKER_PROPERTY)

        val location = arguments?.getDoubleArray(KEY_MY_LOCATION)


        data?.let {
            val from = location?.let { LatLng(it[0], it[1]) }
            val to = LatLng(it.latitude, it.longitude)

            from?.let { from ->
                placeUIData =
                    PlaceUIData(
                        title = it.title ?: "",
                        roadAddress = it.roadAddress ?: "",
                        telePhone = it.telePhone ?: "",
                        category = it.category ?: "",
                        distance = from.toDistanceString(to),
                        distanceDouble = from.toDistance(to)
                    )
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.data = placeUIData
    }

    fun initView() {
    }


    companion object {
        val TAG: String = this::class.java.simpleName

        const val KEY_MARKER_PROPERTY = "KEY_MARKER_PROPERTY"
        const val KEY_MY_LOCATION = "KEY_MY_LOCATION"

        fun newInstance(markerProperty: SHPlace, myLocationArray: DoubleArray) = PreviewFragment()
            .apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_MARKER_PROPERTY, markerProperty)
                    putDoubleArray(KEY_MY_LOCATION, myLocationArray)
                }
                setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundBottomSheetDialog)
            }
    }


}