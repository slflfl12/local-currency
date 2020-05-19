package kr.ac.hansung.gyunggilocalmoneymap

import android.app.Application
import android.util.Log
import androidx.databinding.library.BuildConfig
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import kr.ac.hansung.gyunggilocalmoneymap.di.appModule
import kr.ac.hansung.gyunggilocalmoneymap.di.datasourceModule
import kr.ac.hansung.gyunggilocalmoneymap.di.networkModule
import kr.ac.hansung.gyunggilocalmoneymap.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.io.IOException
import java.net.SocketException

class LocalMapApplication : Application() {

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
            androidContext(this@LocalMapApplication)
            modules(listOf(appModule, repositoryModule, datasourceModule, networkModule))

        }
    }
}