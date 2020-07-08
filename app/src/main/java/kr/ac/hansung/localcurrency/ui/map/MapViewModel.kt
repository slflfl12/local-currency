package kr.ac.hansung.localcurrency.ui.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naver.maps.geometry.LatLng
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.localcurrency.data.repository.OpenApiRepository
import kr.ac.hansung.localcurrency.ui.base.BaseViewModel
import kr.ac.hansung.localcurrency.util.SingleLiveEvent
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.localcurrency.data.repository.NaverMapRepository
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace

class MapViewModel(
    private val openApiRepository: OpenApiRepository,
    private val naverMapRepository: NaverMapRepository
) : BaseViewModel() {


    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation: LiveData<LatLng>
        get() = _currentLocation

    private val _currentMyLocation = MutableLiveData<LatLng>()
    val currentMyLocation: LiveData<LatLng>
        get() = _currentMyLocation

    private val _nearByValue = MutableLiveData<Double>(0.5)
    val nearByValue: LiveData<Double>
        get() = _nearByValue



    private val _places = MutableLiveData<List<SHPlace>>()
    val places: LiveData<List<SHPlace>>
        get() = _places






    private val _getNearBySelectedEvent = SingleLiveEvent<Float>()
    val getNearBySelectedEvent: LiveData<Float>
        get() = _getNearBySelectedEvent


    val loadingSubject = BehaviorSubject.createDefault(false)


    private val _errorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable>
        get() = _errorMessage

    private val _initEvent = SingleLiveEvent<Unit>()
    val initEvent: LiveData<Unit>
        get() = _initEvent

    init {
        /*reqAllPlaces2()*/
    }

    fun reqNearByMaps_X5() {
        currentLocation.value?.let {
            val latitude = it.latitude
            val longitude = it.longitude
            nearByValue.value?.let { nearByValue ->
                openApiRepository.getNearByMaps(latitude, longitude, nearByValue)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        loadingSubject.onNext(true)
                    }
                    .doAfterTerminate {
                        loadingSubject.onNext(false)
                    }
                    .observeOn(Schedulers.io())
                    .subscribe({
                        if (it.isNotEmpty()) {
                            _places.postValue(it)
                        } else {
                            Log.d("결과 없음", "결과 없음")
                        }
                    }, {

                    })
            }

        }
    }




    fun onChangedLocation(latLng: LatLng) {
        if (_currentLocation.value != latLng) {
            _currentLocation.value = latLng
            //getGeocode(latLng)
        }
    }


    fun onChangedMyLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (_currentMyLocation.value != latLng) {
            _currentMyLocation.value = latLng
        }
    }

    fun setNearByValue(distance: Double) {
        _nearByValue.value = distance
    }



    private fun showLoading() {
        loadingSubject.onNext(true)
    }

    private fun hideLoading() {
        loadingSubject.onNext(false)
    }


}