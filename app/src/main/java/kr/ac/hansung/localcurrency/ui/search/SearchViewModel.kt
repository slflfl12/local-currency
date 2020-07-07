package kr.ac.hansung.localcurrency.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naver.maps.geometry.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kr.ac.hansung.localcurrency.data.remote.model.SHPlace
import kr.ac.hansung.localcurrency.data.repository.OpenApiRepository
import kr.ac.hansung.localcurrency.ui.base.BaseViewModel
import java.util.concurrent.TimeUnit

class SearchViewModel(
    private val openApiRepository: OpenApiRepository
) : BaseViewModel() {


    val _query = MutableLiveData<String>()


    private val _places = MutableLiveData<List<SHPlace>>()
    val places: LiveData<List<SHPlace>>
        get() = _places

    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation: LiveData<LatLng>
        get() = _currentLocation

    private val _isProgressBoolean = MutableLiveData<Boolean>()
    val isProgressBoolean: LiveData<Boolean>
        get() = _isProgressBoolean

    private val _isKeyBoardBoolean = MutableLiveData<Boolean>()
    val isKeyBoardBoolean: LiveData<Boolean>
        get() = _isKeyBoardBoolean

    private val _errorResultEmpty = MutableLiveData<Throwable>()
    val errorResultEmpty: LiveData<Throwable>
        get() = _errorResultEmpty

    private val _errorQueryEmpty = MutableLiveData<Throwable>()
    val errorQueryEmpty: LiveData<Throwable>
        get() = _errorQueryEmpty

    private val _errorThrowable = MutableLiveData<Throwable>()
    val errorThrowable: LiveData<Throwable>
        get() = _errorThrowable


    val buttonClickSubject = PublishSubject.create<Unit>()

    init {
        buttonClickSubject.throttleFirst(2L, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                searchPlaces()
            }, {
            }).addTo(compositeDisposable)
    }


    fun searchPlaces() {
        _query.value?.let { query ->
            if (query.isBlank()) {
                _errorQueryEmpty.value = Throwable()
            } else {
                currentLocation.value?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    openApiRepository.getMapsByQuery(
                            query = query.trim(),
                            latitude = latitude,
                            longitude = longitude)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe {
                                showProgress()
                            }
                            .doAfterTerminate {
                                hideProgress()
                                hideKeyBoard()
                            }
                            .doOnError {
                                hideProgress()
                            }
                            .subscribe({
                                it?.let {
                                    if(it.isNotEmpty()) {
                                        _places.value = it
                                    } else {
                                        _errorResultEmpty.value = Throwable()
                                    }
                                }
                            }, {
                                _errorThrowable.value = it
                            }).addTo(compositeDisposable)
                }

            }
        }

    }

    private fun hideKeyBoard() {
        _isKeyBoardBoolean.value = false
    }

    private fun showProgress() {
        _isProgressBoolean.value = true
    }

    private fun hideProgress() {
        _isProgressBoolean.value = false
    }

    fun setCurrentLocation(currentLocation: LatLng) {
        if(_currentLocation.value != currentLocation) {
            _currentLocation.value = currentLocation
        }
    }



}