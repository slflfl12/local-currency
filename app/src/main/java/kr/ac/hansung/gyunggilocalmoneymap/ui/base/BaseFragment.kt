package kr.ac.hansung.gyunggilocalmoneymap.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.databinding.library.baseAdapters.BR

abstract class BaseFragment<B: ViewDataBinding, VM: BaseViewModel>(private val layoutResId: Int) : Fragment() {

    protected lateinit var binding: B

    abstract val vm: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.setVariable(BR.vm, vm)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroyView() {
        vm.clearDisposable()
        super.onDestroyView()
    }

}