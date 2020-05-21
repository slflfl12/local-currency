package kr.ac.hansung.gyunggilocalmoneymap.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.data.MapRepository
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseViewModel
import kr.ac.hansung.gyunggilocalmoneymap.util.SingleLiveEvent
import timber.log.Timber

class SplashViewModel(private val mapRepository: MapRepository) : BaseViewModel() {

    private var appVersion = mapRepository.appVersion



    val _loadingCompleteEvent = SingleLiveEvent<Int>()

    init {
        if(appVersion.equals(BuildConfig.VERSION_NAME)) {
            Log.d("sh", "sh $appVersion")

        } else {
            Log.d("sh", "sh $appVersion")
            appVersion = "aaa"
            saveAll()
        }
    }

    fun saveAll() {
        Timber.d("sh save")
        mapRepository.saveAll()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .subscribe({
                _loadingCompleteEvent.postValue(1)
            }, {
                Log.d("save error", it.toString())
            }).addTo(compositeDisposable)

    }
}