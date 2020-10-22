package kr.ac.hansung.localcurrency.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_splash.*
import kr.ac.hansung.localcurrency.BuildConfig
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.data.repository.OpenApiRepository
import kr.ac.hansung.localcurrency.ui.base.BaseViewModel
import kr.ac.hansung.localcurrency.util.Event

class SplashViewModel(private val openApiRepository: OpenApiRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val appVersion = openApiRepository.appVersion

    val loadingSubject = openApiRepository.pageLoadingSubject
    var loadedDataCompletedSubject = BehaviorSubject.createDefault(false)

    private val _loading = MutableLiveData<Event<Int>>()
    val loading: LiveData<Event<Int>>
        get() = _loading


    private val _errorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable>
        get() = _errorMessage

    init {
        if (appVersion.equals(BuildConfig.VERSION_NAME)) {
            loadedDataCompletedSubject.onNext(true)
            loadingSubject.onNext(270)
        } else {
            saveData()
            loadingSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _loading.value = Event(it)
                }, {

                })
                .addTo(compositeDisposable)
        }
    }

    private fun saveData() {
        compositeDisposable.add(
            openApiRepository.saveData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe({
                    loadedDataCompletedSubject.onNext(true)
                }, {
                    _errorMessage.value = it
                })
        )
    }

    fun getLoadedDataCompleteSubject(): BehaviorSubject<Boolean> {
        return loadedDataCompletedSubject
    }

    fun unBindViewModel() {
        compositeDisposable.clear()
    }

    fun getCompositeDisposable() :CompositeDisposable{
        return compositeDisposable
    }


}