package kr.ac.hansung.localcurrency.ui.base

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<B : ViewDataBinding, VM : BaseViewModel>(private val layoutResId: Int)
    : BottomSheetDialogFragment() {

    protected lateinit var binding: B

    abstract val vm: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.setVariable(BR.vm,vm)
        binding.lifecycleOwner = this
        dialog?.let{
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return binding.root
    }

    override fun onPause() {
        vm.unbindViewModel()
        super.onPause()
    }

    fun showKeyboard() {
        (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,0
        )
    }

    fun hideKeyboard() {
        (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                (activity?.currentFocus ?: View(requireContext())).windowToken, 0
        )
    }


}