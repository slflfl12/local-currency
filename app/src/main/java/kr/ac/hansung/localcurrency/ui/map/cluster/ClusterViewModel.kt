package kr.ac.hansung.localcurrency.ui.map.cluster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.ac.hansung.localcurrency.ui.base.BaseViewModel
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.util.Event

class ClusterViewModel : BaseViewModel() {

    private val _placeClickEvent = MutableLiveData<Event<PlaceUIData>>()
    val placeClickEvent: LiveData<Event<PlaceUIData>>
        get() = _placeClickEvent


    fun onPlaceClickEvent(item: PlaceUIData?) {
        item?.let {
            _placeClickEvent.value = Event(item)
        }
    }
}