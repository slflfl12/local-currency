package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.MapLocalDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.MapLocalDataSourceImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.MapRemoteDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.MapRemoteDataSourceImpl
import org.koin.dsl.module


val datasourceModule = module {

    single<MapLocalDataSource> { MapLocalDataSourceImpl(get(), get()) }
    single<MapRemoteDataSource> { MapRemoteDataSourceImpl(get()) }


}