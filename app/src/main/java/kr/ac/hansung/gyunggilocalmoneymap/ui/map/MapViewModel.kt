package kr.ac.hansung.gyunggilocalmoneymap.ui.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naver.maps.geometry.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.data.OpenApiRepository
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseViewModel
import kr.ac.hansung.gyunggilocalmoneymap.util.SingleLiveEvent
import io.reactivex.rxkotlin.addTo
import kr.ac.hansung.gyunggilocalmoneymap.data.NaverMapRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.util.siguntoSi
import kr.ac.hansung.gyunggilocalmoneymap.util.toForApiString

class MapViewModel(
    private val openApiRepository: OpenApiRepository,
    private val naverMapRepository: NaverMapRepository
) : BaseViewModel() {


    private val _currentCameraSigun = MutableLiveData<String>()
    val currentCameraSigun: LiveData<String>
        get() = _currentCameraSigun

    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation: LiveData<LatLng>
        get() = _currentLocation

    private val _currentMyLocation = MutableLiveData<LatLng>()
    val currentMyLocation: LiveData<LatLng>
        get() = _currentMyLocation

    private val _currentNearByValue = MutableLiveData<Float>(1.5f)
    val currentNearByValue: LiveData<Float>
        get() = _currentNearByValue

    private val _sigunSpinnerItem = MutableLiveData<String>("성남시")
    val sigunSpinnerItem: LiveData<String>
        get() = _sigunSpinnerItem


        private val _places = MutableLiveData<List<SHPlace>>()
    val places: LiveData<List<SHPlace>>
        get() = _places

    private val _sigunSelectedEvent = SingleLiveEvent<String>()
    val sigunSelectedEvent: LiveData<String>
        get() = _sigunSelectedEvent

    private val _getNearBySelectedEvent = SingleLiveEvent<Float>()
    val getNearBySelectedEvent: LiveData<Float>
        get() = _getNearBySelectedEvent









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
        openApiRepository.getMapEntities()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .subscribe({
                _places.postValue(it)
                Log.d("sh mapEntities = ", it.size.toString())
            }, {
                Log.d("sh Error", it.toString())
            })
            .addTo(compositeDisposable)
    }
    

    private fun getGeocode(coords: LatLng) {
        naverMapRepository.getGeocode(coords.toForApiString())
            .subscribeOn(Schedulers.io())
            .map{ it.results[0].region.area2.name.siguntoSi() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({sigun ->
                sigun?.let {
                    _currentCameraSigun.value = it
                }
                Log.d("sh sigun", sigun)
            }
            ,{

                }).addTo(compositeDisposable)
    }

    private fun getPlacesBySi(si: String) {
        openApiRepository.getPlacesBySi(si)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                _places.value = it
            }, {

            }).addTo(compositeDisposable)
    }

    fun onClickNearByRefresh() {
    }


    fun onChangedLocation(latLng: LatLng) {
        if (_currentLocation.value != latLng) {
            _currentLocation.value = latLng
            getGeocode(latLng)
        }
    }

    fun onChangedMyLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (_currentMyLocation.value != latLng) {
            _currentMyLocation.value = latLng
        }
    }

    fun setNearByValue(distance: Float) {
        _currentNearByValue.value = distance
    }

    fun setSigunItem(sigun: String) {
        _sigunSpinnerItem.value = sigun
        Log.d("sh sigunSpinner", sigun)
    }


    private fun init() {
        _initEvent.call()
    }

}