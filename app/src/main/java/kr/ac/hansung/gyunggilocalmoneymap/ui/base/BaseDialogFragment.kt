package kr.ac.hansung.gyunggilocalmoneymap.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onDestroyView() {
        vm.unbindViewModel()
        super.onDestroyView()
    }
}