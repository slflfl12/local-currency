package kr.ac.hansung.gyunggilocalmoneymap

import android.app.Application
import androidx.databinding.library.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.di.appModule
import kr.ac.hansung.gyunggilocalmoneymap.di.datasourceModule
import kr.ac.hansung.gyunggilocalmoneymap.di.networkModule
import kr.ac.hansung.gyunggilocalmoneymap.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LocalMapApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) androidLogger()
            androidContext(this@LocalMapApplication)
            modules(listOf(appModule, repositoryModule, datasourceModule, networkModule))

        }
    }
}