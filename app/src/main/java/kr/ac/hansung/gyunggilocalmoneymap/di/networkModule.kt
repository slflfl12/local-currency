package kr.ac.hansung.gyunggilocalmoneymap.di

import androidx.room.Room
import kr.ac.hansung.gyunggilocalmoneymap.BuildConfig
import kr.ac.hansung.gyunggilocalmoneymap.data.local.MapDatabase
import kr.ac.hansung.gyunggilocalmoneymap.data.local.pref.PreferencesHelper
import kr.ac.hansung.gyunggilocalmoneymap.data.local.pref.PreferencesHelperImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.network.ReverseGeoCodeService
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.network.OpenApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private const val OPEN_API_BASE_URL = "https://openapi.gg.go.kr/"
private const val NAVER_REVERSE_GEOCODE_BASE_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/"


val networkModule = module {


    //remote
    single(named("OpenApiClient")) {
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
            .build()
    }

    single(named(name = "ReverseGeocodeClient")) {
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }).addInterceptor(get(named("ReverseGeocodeInterceptor")))
            .build()
    }


    single(named(name = "ReverseGeocodeInterceptor")) {
        Interceptor {chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("X-NCP-APIGW-API-KEY-ID", "0yzlp5bje8")
                    .addHeader("X-NCP-APIGW-API-KEY", "90rJlh3ZBoJEqdIS1JKSWhhQxYbLreY3wgiTuNyg")
                    .build()
            )
        }
    }

    single {
        GsonConverterFactory.create()
    }

    single {
        RxJava2CallAdapterFactory.create()
    }

    single<OpenApiService> {
        Retrofit.Builder()
            .baseUrl(OPEN_API_BASE_URL)
            .client(get(named("OpenApiClient")))
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .build()
            .create(OpenApiService::class.java)
    }

    single<ReverseGeoCodeService> {
        Retrofit.Builder()
            .baseUrl(NAVER_REVERSE_GEOCODE_BASE_URL)
            .client(get(named("ReverseGeocodeClient")))
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .build()
            .create(ReverseGeoCodeService::class.java)
    }

    // local
    single {
        Room.databaseBuilder(androidContext(), MapDatabase::class.java, "map.db")
            .allowMainThreadQueries().build()
    }

    single {
        get<MapDatabase>().mapDao()
    }

    single<PreferencesHelper> {
        PreferencesHelperImpl(androidContext())
    }

}