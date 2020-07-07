package kr.ac.hansung.localcurrency.ui.map.cluster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.naver.maps.geometry.LatLng
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import kr.ac.hansung.localcurrency.databinding.DialogClusterBinding
import kr.ac.hansung.localcurrency.ui.base.BaseDialogFragment
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.util.toDistance
import kr.ac.hansung.localcurrency.util.toDistanceString
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClusterDialog : BaseDialogFragment<DialogClusterBinding, ClusterViewModel>(R.layout.dialog_cluster), ClusterAdapter.OnItemClickListener {

    private lateinit var clusterAdapter: ClusterAdapter
    private var markerLocation: LatLng? = null
    override val vm: ClusterViewModel by viewModel()




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialog = this
        initView()

        arguments?.getDoubleArray(KEY_MARKER_LOCATION)?.let {
            markerLocation = LatLng(it[0], it[1])
            Log.d("locations", it.toString())
        }

        arguments?.getParcelableArrayList<SHPlace>(KEY_PLACE_LIST)?.let {
            Log.d("locations", it.toString())
            it.map {
                markerLocation?.let { currentLocation ->
                    PlaceUIData(
                            title = it.title ?: "",
                            roadAddress = it.roadAddress ?: "",
                            telePhone = it.telePhone ?: "",
                            category = it.category ?: "",
                            distance = currentLocation.toDistanceString(LatLng(it.latitude, it.longitude)),
                            distanceDouble = currentLocation.toDistance(LatLng(it.latitude, it.longitude))
                    )
                }
            }.run {
                sortedBy { it!!.distanceDouble }
            }.run {
                if (::clusterAdapter.isInitialized)
                    clusterAdapter.submitList(this)
            }
        }
    }


    private fun initView() {
        clusterAdapter = ClusterAdapter().apply {
            onItemClickListener = this@ClusterDialog
        }
        binding.rvCluster.adapter = clusterAdapter
    }

    fun onCancelClick() {
        this.dismiss()
    }

    override fun onPlaceClick(placeUIData: PlaceUIData) {

    }



    companion object {

        val TAG = this::class.java.simpleName

        val KEY_PLACE_LIST = "KEY_PLACE_LIST"
        val KEY_MARKER_LOCATION = "KEY_MARKER_LOCATION"


        fun newInstance(list: ArrayList<SHPlace>, locationArray: DoubleArray) = ClusterDialog().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(KEY_PLACE_LIST, list)
                putDoubleArray(KEY_MARKER_LOCATION, locationArray)
            }
            setStyle(STYLE_NORMAL, R.style.ThemeOverlay_AppCompat_Dialog)
        }

    }

}


