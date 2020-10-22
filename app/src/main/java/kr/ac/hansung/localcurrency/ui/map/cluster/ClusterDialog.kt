package kr.ac.hansung.localcurrency.ui.map.cluster

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.naver.maps.geometry.LatLng
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import kr.ac.hansung.localcurrency.databinding.DialogClusterBinding
import kr.ac.hansung.localcurrency.ui.base.BaseDialogFragment
import kr.ac.hansung.localcurrency.ui.detail.DetailActivity
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.util.EventObserver
import kr.ac.hansung.localcurrency.util.toDistance
import kr.ac.hansung.localcurrency.util.toDistanceString
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClusterDialog : BaseDialogFragment<DialogClusterBinding, ClusterViewModel>(R.layout.dialog_cluster) {


    override val vm: ClusterViewModel by viewModel()

    private lateinit var clusterAdapter: ClusterAdapter
    private var markerLocation: LatLng? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialog = this
        initAdapter()

        arguments?.getDoubleArray(KEY_MARKER_LOCATION)?.let {
            markerLocation = LatLng(it[0], it[1])
        }

        arguments?.getParcelableArrayList<SHPlace>(KEY_PLACE_LIST)?.let {

            it.map {
                markerLocation?.let { currentLocation ->
                    PlaceUIData(
                            title = it.title ?: "",
                            roadAddress = it.roadAddress ?: "",
                            telePhone = it.telePhone ?: "",
                            category = it.category ?: "",
                            latitude = it.latitude,
                            longitude = it.longitude,
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

        initView()
        initObserve()
    }

    private fun initAdapter() {
        clusterAdapter = ClusterAdapter(vm)
        binding.rvCluster.adapter = clusterAdapter
    }


    private fun initView() {


    }

    private fun initObserve() {
        vm.placeClickEvent.observe(this@ClusterDialog,
                EventObserver(this@ClusterDialog::onPlaceClick))
    }

    fun onCancelClick() {
        this.dismiss()
    }

    private fun onPlaceClick(placeUIData: PlaceUIData?) {
        Intent(context, DetailActivity::class.java).apply {
            markerLocation?.let {
                putExtra(DetailActivity.KEY_LOCATION, doubleArrayOf(it.latitude, it.longitude))
                putExtra(DetailActivity.KEY_PLACE, placeUIData)
            }.also {
                startActivity(it)
            }
        }
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

        val TAG = this::class.java.simpleName

        const val KEY_PLACE_LIST = "KEY_PLACE_LIST"
        const val KEY_MARKER_LOCATION = "KEY_MARKER_LOCATION"


        fun newInstance(list: ArrayList<SHPlace>, locationArray: DoubleArray) = ClusterDialog().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(KEY_PLACE_LIST, list)
                putDoubleArray(KEY_MARKER_LOCATION, locationArray)
            }
            setStyle(STYLE_NORMAL, R.style.ThemeOverlay_AppCompat_Dialog)
        }

    }

}


