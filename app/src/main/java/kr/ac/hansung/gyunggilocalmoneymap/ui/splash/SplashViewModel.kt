package kr.ac.hansung.gyunggilocalmoneymap.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.data.MapRepository
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseViewModel
import kr.ac.hansung.gyunggilocalmoneymap.util.SingleLiveEvent
import timber.log.Timber

class SplashViewModel(private val mapRepository: MapRepository) : BaseViewModel() {

    private val appVersion = mapRepository.appVersion

    private val _pageLoading = MutableLiveData<Float>()
    val pageLoading: LiveData<Float>
        get() = _pageLoading

    val _loadingCompleteEvent = SingleLiveEvent<Int>()

    init {
        if (appVersion.equals(BuildConfig.VERSION_NAME)) {
            Log.d("sh", "sh $appVersion")
            _loadingCompleteEvent.postValue(1)
        } else {
            Log.d("sh", "sh $appVersion")
            saveAll()
            pageLoading()
        }
    }

    private fun saveAll() {
        Log.d("sh saveAll", "sh saveAll()")
        compositeDisposable.add(
            mapRepository.saveAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe({
                    _loadingCompleteEvent.postValue(1)
                }, {
                    Log.d("sh save error", it.toString())
                })
        )
    }

    private fun pageLoading() {
        mapRepository.pageLoadingSubject
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                _pageLoading.value = it
                Log.d("sh loading", it.toString())
            }, {

            }).addTo(compositeDisposable)
    }
}