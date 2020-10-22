package kr.ac.hansung.localcurrency.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun unbindViewModel() {
        compositeDisposable.clear()
    }


}