package kr.ac.hansung.gyunggilocalmoneymap.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.data.OpenApiRepository
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseViewModel
import kr.ac.hansung.gyunggilocalmoneymap.util.SingleLiveEvent

class SplashViewModel(private val openApiRepository: OpenApiRepository) : BaseViewModel() {

    private val appVersion = openApiRepository.appVersion

    val loadingSubject = openApiRepository.pageLoadingSubject

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
            openApiRepository.saveAll()
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
        openApiRepository.pageLoadingSubject
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                _pageLoading.value = it
                Log.d("sh loading", it.toString())
            }, {

            }).addTo(compositeDisposable)
    }
}