package kr.ac.hansung.localcurrency.ui.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<B : ViewDataBinding, VM : BaseViewModel>(private val layoutResId: Int)
    : DialogFragment() {

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

        return binding.root
    }

    override fun onStart() {
        super.onStart()


        dialog?.let{
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setCanceledOnTouchOutside(false)
        }
    }

    override fun onResume() {
        super.onResume()


        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = (metrics.widthPixels * 0.8).toInt()
        val height = (metrics.heightPixels * 0.8).toInt()
        dialog?.window?.setLayout(width, height)
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