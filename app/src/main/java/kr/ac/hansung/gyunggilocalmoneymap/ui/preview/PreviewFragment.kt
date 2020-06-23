package kr.ac.hansung.gyunggilocalmoneymap.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.databinding.FragmentPreviewBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseBottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreviewFragment : BaseBottomSheetDialogFragment<FragmentPreviewBinding, PreviewViewModel>(R.layout.fragment_preview){

    override val vm: PreviewViewModel by viewModel()



    companion object {
        val TAG: String = this::class.java.simpleName

        const val KEY_MARKER_PROPERTY = "KEY_MARKER_PROPERTY"

        fun newInstance(markerProperty: SHPlace) = PreviewFragment().apply {
            arguments =Bundle().apply {
            }
        }
    }
}