package kr.ac.hansung.gyunggilocalmoneymap.ui.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.naver.maps.geometry.LatLng
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.data.MapRepository
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseViewModel
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse.RegionMnyFacltStu.Place
import kr.ac.hansung.gyunggilocalmoneymap.util.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import timber.log.Timber

class MapViewModel(
    private val mapRepository: MapRepository
) : BaseViewModel() {


    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation: LiveData<LatLng>
        get() = _currentLocation

    private val _currentMyLocation = MutableLiveData<LatLng>()
    val currentMyLocation: LiveData<LatLng>
        get() = _currentMyLocation


    private val _mapEntities = MutableLiveData<List<SHPlace>>()
    val mapEntities: LiveData<List<SHPlace>>
        get() = _mapEntities



/*    private val _placeArrayDatas: LiveData<Event<ArrayList<Place>>> = Transformations.map(_placeDatas) {
        val list = ArrayList(it)
        Event(list)
    }
    val placeArrayDatas: LiveData<Event<ArrayList<Place>>>
        get() = _placeArrayDatas*/


    private val _errorResult = MutableLiveData<Throwable>()
    val errorResult: LiveData<Throwable>
        get() = _errorResult

    private val _initEvent = SingleLiveEvent<Unit>()
    val initEvent: LiveData<Unit>
        get() = _initEvent

    init {

    }

    fun getMapEntities() {
        mapRepository.getMapEntities()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .subscribe({
                _mapEntities.postValue(it)
                Log.d("sh mapEntities = ", it.size.toString())
            }, {
                Log.d("sh Error", it.toString())
            })
            .addTo(compositeDisposable)
    }


    fun onChangedLocation(latLng: LatLng) {
        if (_currentLocation.value != latLng) {
            _currentLocation.value = latLng
        }
    }

    fun onChangedMyLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (_currentMyLocation.value != latLng) {
            _currentMyLocation.value = latLng
        }
    }


    private fun init() {
        _initEvent.call()
    }

}