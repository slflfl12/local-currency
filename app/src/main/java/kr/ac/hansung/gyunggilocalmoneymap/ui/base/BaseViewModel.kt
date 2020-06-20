package kr.ac.hansung.gyunggilocalmoneymap.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel(){

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun unbindViewModel() {
        compositeDisposable.clear()
    }


}