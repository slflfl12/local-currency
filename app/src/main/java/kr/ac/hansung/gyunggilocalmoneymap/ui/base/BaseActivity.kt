package kr.ac.hansung.gyunggilocalmoneymap.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(private val layoutResId: Int) : AppCompatActivity() {

    protected lateinit var binding: B

    abstract val vm: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.setVariable(BR.vm, vm)
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        vm.clearDisposable()
        super.onDestroy()
    }

    protected fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}