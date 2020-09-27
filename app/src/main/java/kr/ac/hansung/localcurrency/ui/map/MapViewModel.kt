package kr.ac.hansung.localcurrency.ui.map

import android.location.Location
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
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.util.Event

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

    private val _distanceValue = MutableLiveData<Double>(0.5)
    val distanceValue: LiveData<Double>
        get() = _distanceValue

    private val _places = MutableLiveData<List<SHPlace>>()
    val places: LiveData<List<SHPlace>>
        get() = _places


    private val _bottomSheetDialogState = MutableLiveData<Int>()
    val bottomSheetDialogState: LiveData<Int>
        get() = _bottomSheetDialogState

    private val _bottomSheetDialogShow = MutableLiveData<Boolean>()
    val showBottomSheetDialog: LiveData<Boolean>
        get() = _bottomSheetDialogShow


    private val _getNearBySelectedEvent = SingleLiveEvent<Float>()
    val getNearBySelectedEvent: LiveData<Float>
        get() = _getNearBySelectedEvent

    private val _bottomSheetcloseEvent = SingleLiveEvent<Unit>()
    val closeEvent: LiveData<Unit>
        get() = _bottomSheetcloseEvent

    private val _itemClickEvent = MutableLiveData<Event<PlaceUIData>>()
    val itemClickEvent: LiveData<Event<PlaceUIData>>
        get() = _itemClickEvent

    private val _navigateToCallEvent = MutableLiveData<Event<PlaceUIData>>()
    val navigateToCallEvent: LiveData<Event<PlaceUIData>>
        get() = _navigateToCallEvent

    private val _navigateToFindLoadEvent = MutableLiveData<Event<PlaceUIData>>()
    val navigateToFindLoadEvent: LiveData<Event<PlaceUIData>>
        get() = _navigateToFindLoadEvent

/*    private val _bottomSheetStateEvent = MutableLiveData<Event<Int>>(Event(BottomSheetBehavior.STATE_COLLAPSED))
    val bottomSheetStateEvent: LiveData<Event<Int>>
        get() = _bottomSheetStateEvent*/

    private val _fabClickEvent = SingleLiveEvent<Unit>()
    val fabClickEvent: LiveData<Unit>
        get() = _fabClickEvent

    private val _fab1ClickEvent = SingleLiveEvent<Unit>()
    val fab1ClickEvent: LiveData<Unit>
        get() = _fab1ClickEvent

    private val _fab2ClickEvent = SingleLiveEvent<Unit>()
    val fab2ClickEvent: LiveData<Unit>
        get() = _fab2ClickEvent

    private val _navButtonEvent = SingleLiveEvent<Unit>()
    val navButtonEvent: LiveData<Unit>
        get() = _navButtonEvent


    val loadingSubject = BehaviorSubject.createDefault(false)


    private val _errorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable>
        get() = _errorMessage

    private val _initEvent = SingleLiveEvent<Unit>()
    val initEvent: LiveData<Unit>
        get() = _initEvent


    fun reqNearByMaps() {
        currentLocation.value?.let {
            val latitude = it.latitude
            val longitude = it.longitude
            distanceValue.value?.let { nearByValue ->
                openApiRepository.getNearByMaps(latitude, longitude, nearByValue)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe {
                            showLoading()
                        }
                        .doAfterTerminate {
                            hideLoading()
                        }
                        .observeOn(Schedulers.io())
                        .subscribe({
                            if (it.isNotEmpty()) {
                                _places.postValue(it)
                            } else {
                                _errorMessage.postValue(Throwable())
                            }
                        }, {
                            _errorMessage.postValue(it)
                        })
            }

        }
    }


    fun onItemClick(placeUIData: PlaceUIData?) {
        placeUIData?.let {
            _itemClickEvent.value = Event(it)
        }
    }

    fun onNavigateCall(placeUIData: PlaceUIData?) {
        placeUIData?.let {
            _navigateToCallEvent.value = Event(it)
        }
    }


    fun onNavigateFindLoad(placeUIData: PlaceUIData?) {
        placeUIData?.let {
            _navigateToFindLoadEvent.value = Event(it)
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

    fun onFabClick() {
        _fabClickEvent.call()
    }

    fun onFab1Click() {
        _distanceValue.value = 0.3
        _fab1ClickEvent.call()
    }

    fun onFab2Click() {
        _distanceValue.value = 0.5
        _fab2ClickEvent.call()
    }

    fun setNearByValue(distance: Double) {
        _distanceValue.value = distance
    }

    fun onNavButtonClick() {
        _navButtonEvent.call()
    }

    fun showBottomSheetDialog() {
        _bottomSheetDialogShow.value = true
    }

    fun onBottomSheetClose() {
        _bottomSheetcloseEvent.call()
    }

    private fun showLoading() {
        loadingSubject.onNext(true)
    }

    private fun hideLoading() {
        loadingSubject.onNext(false)
    }


}