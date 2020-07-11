package kr.ac.hansung.localcurrency.ui.map.preview

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.naver.maps.geometry.LatLng
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import kr.ac.hansung.localcurrency.databinding.FragmentPreviewBinding
import kr.ac.hansung.localcurrency.ui.base.BaseBottomSheetDialogFragment
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.util.splitPhoneNum
import kr.ac.hansung.localcurrency.util.toDistance
import kr.ac.hansung.localcurrency.util.toDistanceString
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreviewFragment :
        BaseBottomSheetDialogFragment<FragmentPreviewBinding, PreviewViewModel>(R.layout.fragment_preview) {

    override val vm: PreviewViewModel by viewModel()

    private lateinit var placeUIData: PlaceUIData
    private var myLocation: DoubleArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = arguments?.getParcelable<SHPlace>(KEY_MARKER_PROPERTY)

        arguments?.getDoubleArray(KEY_MY_LOCATION)?.let {
            myLocation = it
        }


        data?.let {
            val from = myLocation?.let { LatLng(it[0], it[1]) }
            val to = LatLng(it.latitude, it.longitude)

            from?.let { from ->
                placeUIData =
                        PlaceUIData(
                                title = it.title ?: "",
                                roadAddress = it.roadAddress ?: "",
                                telePhone = it.telePhone ?: "",
                                category = it.category ?: "",
                                latitude = it.latitude,
                                longitude = it.longitude,
                                distance = from.toDistanceString(to),
                                distanceDouble = from.toDistance(to)
                        )
            }
        }




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.data = placeUIData
        initView()
    }

    fun initView() {
        binding.tvCall.setOnClickListener {
            placeUIData.telePhone.let {
                startActivity(Intent(Intent.ACTION_DIAL, ("tel:${it.splitPhoneNum()}").toUri()))
            }
        }

        binding.tvFindLoad.setOnClickListener {
            findLoad(placeUIData)
        }

        binding.ivAddress.setImageResource(R.drawable.ic_location)
        binding.ivAddress.visibility = View.VISIBLE
    }



    fun findLoad(placeUIData: PlaceUIData) {
        val url = "nmap://route/walk?dlat=${placeUIData.latitude}&dlng=${placeUIData.longitude}&dname=${placeUIData.title}&appname=kr.ac.hansung.localcurrency"
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        val list: List<ResolveInfo> = activity?.getPackageManager()?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY) as List<ResolveInfo>

        if (list.isEmpty()) {
            context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.nmap")))
        } else {
            context?.startActivity(intent)
        }
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
                    setStyle(DialogFragment.STYLE_NO_TITLE, R.style.RoundBottomSheetDialog)
                }
    }

    interface PreviewListener {
        fun onClosePreview()
    }


}