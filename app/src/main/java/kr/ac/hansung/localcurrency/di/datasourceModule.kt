package kr.ac.hansung.localcurrency.di

import kr.ac.hansung.localcurrency.data.local.source.OpenApiLocalDataSource
import kr.ac.hansung.localcurrency.data.local.source.OpenApiLocalDataSourceImpl
import kr.ac.hansung.localcurrency.data.remote.source.NaverMapRemoteDataSource
import kr.ac.hansung.localcurrency.data.remote.source.NaverMapRemoteDataSourceImpl
import kr.ac.hansung.localcurrency.data.remote.source.OpenApiRemoteDataSource
import kr.ac.hansung.localcurrency.data.remote.source.OpenApiRemoteDataSourceImpl
import org.koin.dsl.module


val datasourceModule = module {

    single<OpenApiLocalDataSource> { OpenApiLocalDataSourceImpl(get(), get()) }
    single<OpenApiRemoteDataSource> { OpenApiRemoteDataSourceImpl(get()) }
    single<NaverMapRemoteDataSource> { NaverMapRemoteDataSourceImpl(get()) }

}