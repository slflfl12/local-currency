package kr.ac.hansung.localcurrency.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.localcurrency.BuildConfig
import kr.ac.hansung.localcurrency.data.repository.OpenApiRepository
import kr.ac.hansung.localcurrency.ui.base.BaseViewModel

class SplashViewModel(private val openApiRepository: OpenApiRepository) : BaseViewModel() {

    private val appVersion = openApiRepository.appVersion

    val loadingSubject = openApiRepository.pageLoadingSubject
    val loadedDataCompleteSubject = openApiRepository.loadedDataCompletedSubject

    private val _errorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable>
        get() = _errorMessage

    init {
        if (appVersion.equals(BuildConfig.VERSION_NAME)) {
            loadedDataCompleteSubject.onNext(true)
            loadingSubject.onNext(270)
        } else {
            saveData()
        }
    }

    private fun saveData() {
        compositeDisposable.add(
            openApiRepository.saveData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe({
                    loadedDataCompleteSubject.onNext(true)
                }, {
                    _errorMessage.value = it
                })
        )
    }

}