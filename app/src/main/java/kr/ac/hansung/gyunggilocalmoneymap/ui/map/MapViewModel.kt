package kr.ac.hansung.gyunggilocalmoneymap.ui.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naver.maps.geometry.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.data.LocalMapRepository
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseViewModel

class MapViewModel(private val localMapRepository: LocalMapRepository) : BaseViewModel() {


    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation: LiveData<LatLng>
        get() = _currentLocation

    private val _currentMyLocation = MutableLiveData<LatLng>()
    val currentMyLocation: LiveData<LatLng>
        get() = _currentMyLocation


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

    fun initMap() {
        compositeDisposable.add(localMapRepository.getAllPlace()
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe({
                println("response = $it")


            }, {

            })
        )
    }

}