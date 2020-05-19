package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.MapLocalDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.local.source.MapLocalDataSourceImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.FirebaseDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.FirebaseDataSourceImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.MapRemoteDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.MapRemoteDataSourceImpl
import org.koin.dsl.module


val datasourceModule = module {

    single<MapRemoteDataSource> { MapRemoteDataSourceImpl(get()) }
    single<FirebaseDataSource> { FirebaseDataSourceImpl() }
    single<MapLocalDataSource> { MapLocalDataSourceImpl(get())}
}