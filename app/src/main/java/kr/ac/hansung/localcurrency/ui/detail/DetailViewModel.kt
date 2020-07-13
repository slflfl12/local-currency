package kr.ac.hansung.localcurrency.ui.detail

import androidx.lifecycle.LiveData
import kr.ac.hansung.localcurrency.ui.base.BaseViewModel
import kr.ac.hansung.localcurrency.util.SingleLiveEvent

class DetailViewModel : BaseViewModel() {

    private val _navigateToCallEvent = SingleLiveEvent<Unit>()
    val navigateToCallEvent: LiveData<Unit>
        get() = _navigateToCallEvent

    private val _navigateToFindLoadEvent = SingleLiveEvent<Unit>()
    val navigateToFindLoadEvent: LiveData<Unit>
        get() = _navigateToFindLoadEvent

    fun onNavigateToCall() {
        _navigateToCallEvent.call()
    }

    fun onNavigateFindLoad() {
        _navigateToFindLoadEvent.call()
    }

}