package kr.ac.hansung.gyunggilocalmoneymap.ui.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.naver.maps.geometry.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.data.LocalMapRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseViewModel
import kr.ac.hansung.gyunggilocalmoneymap.data.model.LocalMapResponse.RegionMnyFacltStu.Place
import kr.ac.hansung.gyunggilocalmoneymap.util.Event
import kr.ac.hansung.gyunggilocalmoneymap.util.SingleLiveEvent
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import kr.ac.hansung.gyunggilocalmoneymap.data.FirebaseRepository
import timber.log.Timber

class MapViewModel(private val localMapRepository: LocalMapRepository,
                   private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {


    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation: LiveData<LatLng>
        get() = _currentLocation

    private val _currentMyLocation = MutableLiveData<LatLng>()
    val currentMyLocation: LiveData<LatLng>
        get() = _currentMyLocation

    val _placeDatas = MutableLiveData<ArrayList<Place>>()

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

    fun saveDatas() {

        compositeDisposable.add(Observable.fromIterable(1..570)
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                Schedulers.single()
            )
            .concatMap {
                localMapRepository.getPlaces(it.toString()).toObservable()
            }
            .concatMapCompletable {
                firebaseRepository.savePlaces(it.regionMnyFacltStus[1].places)
            }
            .subscribe({
            }, {

            })
        )


/*        compositeDisposable.add(localMapRepository.getPlaces("1")
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe({
                Log.d("size", it.regionMnyFacltStus[1].places.size.toString())
                _placeDatas.value = ArrayList(it.regionMnyFacltStus[1].places)

                Log.d("load", "load")
            }, {

            })
        )*/

    }

    fun init() {
        _initEvent.call()
    }
}