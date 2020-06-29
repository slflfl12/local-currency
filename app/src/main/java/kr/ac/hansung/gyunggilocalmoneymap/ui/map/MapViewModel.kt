package kr.ac.hansung.gyunggilocalmoneymap.ui.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naver.maps.geometry.LatLng
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.data.repository.OpenApiRepository
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseViewModel
import kr.ac.hansung.gyunggilocalmoneymap.util.SingleLiveEvent
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.data.repository.NaverMapRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.SHPlace
import kr.ac.hansung.gyunggilocalmoneymap.util.showToast
import kr.ac.hansung.gyunggilocalmoneymap.util.splitFirst
import kr.ac.hansung.gyunggilocalmoneymap.util.toForApiString
import java.util.concurrent.Executors

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

    private val _currentMyLocationSigun = MutableLiveData<String>()
    val currentMyLocationSigun: LiveData<String>
        get() = _currentMyLocationSigun

    private val _currentCameraSigun = MutableLiveData<String>().apply { postValue("성남시")}
    val currentCameraSigun: LiveData<String>
        get() = _currentCameraSigun

    private val _currentNearByValue = MutableLiveData<Float>().apply {postValue(1.5f)}
    val currentNearByValue: LiveData<Float>
        get() = _currentNearByValue

    private val _sigunSpinnerItem = MutableLiveData<String>()
    val sigunSpinnerItem: LiveData<String>
        get() = _sigunSpinnerItem


    private val _places = MutableLiveData<List<SHPlace>>()
    val places: LiveData<List<SHPlace>>
        get() = _places

    private val _sigunPlaces = MutableLiveData<ArrayList<SHPlace>>()
    val sigunPlaces: LiveData<ArrayList<SHPlace>>
        get() = _sigunPlaces

    private val _nearByPlaces = MutableLiveData<ArrayList<SHPlace>>()
    val nearByPlaces: LiveData<ArrayList<SHPlace>>
        get() = _nearByPlaces

    private val _allPlaces = MutableLiveData<ArrayList<SHPlace>>()
    val allPlaces: LiveData<ArrayList<SHPlace>>
        get() = _allPlaces


    private val _sigunSelectedEvent = SingleLiveEvent<String>()
    val sigunSelectedEvent: LiveData<String>
        get() = _sigunSelectedEvent

    private val _getNearBySelectedEvent = SingleLiveEvent<Float>()
    val getNearBySelectedEvent: LiveData<Float>
        get() = _getNearBySelectedEvent




    val _sigunStringList = MutableLiveData<List<String>>()


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



    fun reqGeocode(coords: LatLng) {
        naverMapRepository.getGeocode(coords.toForApiString())
            .subscribeOn(Schedulers.io())
            .map { it.results[0].region.area2.name.splitFirst() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("shGetGeocode", it)
                it?.let {sigun ->
                    _sigunStringList.value?.let { list ->
                        if(list.contains(sigun)) {
                            _sigunSpinnerItem.value = sigun
                        }
                    }

                }

            }
                , {
                    _errorMessage.value = it
                }).addTo(compositeDisposable)
    }

    fun reqPlacesBySigun(sigun: String) {
        openApiRepository.getPlacesBySigun(sigun)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                loadingSubject.onNext(true)
            }
            .doAfterTerminate {
                loadingSubject.onNext(false)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _places.value = it
            }, {
                _errorMessage.value = it
            }).addTo(compositeDisposable)
    }

    fun reqNearByPlaces(currentLocation: LatLng) {
        openApiRepository.getNearByPlaces(currentLocation)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                loadingSubject.onNext(true)
            }
            .doAfterTerminate {
                loadingSubject.onNext(false)
            }
            .observeOn(Schedulers.io())
            .subscribe({
                Log.d("list isze", it.size.toString())
                if(it.isNotEmpty()) {
                    _places.postValue(it)
                } else {
                    Log.d("결과 없음", "결과 없음")
                }
            }, {

            }).addTo(compositeDisposable)
    }

    fun reqAllPlaces2() {
        openApiRepository.getAllPlacesPrev()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                loadingSubject.onNext(true)
                Log.d("sh loading", "sh loading")
            }
            .doAfterTerminate {
                loadingSubject.onNext(false)
                Log.d("sh loading", "sh loading")
            }
            .observeOn(Schedulers.io())
            .subscribe({
                _allPlaces.postValue(ArrayList(it))
            }, {

            }).addTo(compositeDisposable)
    }

/*    fun reqAllPlaces() {
        openApiRepository.getAllPlaces()
            .subscribeOn(Schedulers.io())
            .flatMap {
                Observable.fromCallable {
                    _allPlaces.value?.add(it)
                }
            }
            .doOnSubscribe {
                loadingSubject.onNext(true)
                Log.d("sh loading", "sh loading")
            }
            .doAfterTerminate {
                loadingSubject.onNext(false)
                Log.d("sh loading", "sh loading")
            }
            .observeOn(Schedulers.io())
            .subscribe({
                Log.d("done", "done")
            }, {

            }).addTo(compositeDisposable)
    }*/


    fun onClickNearByRefresh() {
        Log.d("sh showData", _allPlaces.value!!.size.toString())
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

    fun setNearByValue(distance: Float) {
        _currentNearByValue.value = distance
    }

    fun setSigunItem(sigun: String) {
        _sigunSpinnerItem.value = sigun
        Log.d("sh sigunSpinner", sigun)
    }


    fun init() {
        _initEvent.call()
    }

}