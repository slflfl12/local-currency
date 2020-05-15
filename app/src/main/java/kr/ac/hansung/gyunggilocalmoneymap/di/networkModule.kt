package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.network.OpenApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private const val NAVER_BASE_URL = "https://openapi.gg.go.kr/"


val networkModule = module {

    single {
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
            .build()
    }

    single {
        GsonConverterFactory.create()
    }

    single {
        RxJava2CallAdapterFactory.create()
    }

    single<OpenApiService> {
        Retrofit.Builder()
            .baseUrl(NAVER_BASE_URL)
            .client(get())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .build()
            .create(OpenApiService::class.java)
    }

}