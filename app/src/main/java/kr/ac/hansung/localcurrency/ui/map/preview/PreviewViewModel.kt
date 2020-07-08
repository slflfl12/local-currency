package kr.ac.hansung.localcurrency.ui.map.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.ac.hansung.localcurrency.ui.base.BaseViewModel
import kr.ac.hansung.localcurrency.util.SingleLiveEvent

class PreviewViewModel : BaseViewModel() {


    private val _phoneData = MutableLiveData<String>()
    val phoneData: LiveData<String>
        get() = _phoneData


    fun setCallEvent(value: String) {
        _phoneData.value = value
    }


}