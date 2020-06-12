package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.OpenApiLocalDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.OpenApiLocalDataSourceImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.NaverMapRemoteDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.NaverMapRemoteDataSourceImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.OpenApiRemoteDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.OpenApiRemoteDataSourceImpl
import org.koin.dsl.module


val datasourceModule = module {

    single<OpenApiLocalDataSource> { OpenApiLocalDataSourceImpl(get(), get()) }
    single<OpenApiRemoteDataSource> { OpenApiRemoteDataSourceImpl(get()) }
    single<NaverMapRemoteDataSource> { NaverMapRemoteDataSourceImpl(get())}

}