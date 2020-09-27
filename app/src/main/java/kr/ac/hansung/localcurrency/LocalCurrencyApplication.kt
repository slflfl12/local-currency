package kr.ac.hansung.localcurrency

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.library.BuildConfig
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import kr.ac.hansung.localcurrency.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.io.IOException
import java.net.SocketException

class LocalCurrencyApplication : Application() {

    @SuppressLint("LongLogTag")
    override fun onCreate() {
        super.onCreate()




        RxJavaPlugins.setErrorHandler { e ->
            var error = e
            if (error is UndeliverableException) {
                error = e.cause
            }
            if (error is IOException || error is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (error is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (error is NullPointerException || error is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            if (error is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            Log.w("Undeliverable exception received, not sure what to do", error)
        }



        startKoin {
            if (BuildConfig.DEBUG) androidLogger()
            androidContext(this@LocalCurrencyApplication)
            modules(listOf(appModule, repositoryModule, datasourceModule, networkModule))

        }
    }
}