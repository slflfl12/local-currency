package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.data.FirebaseRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.FirebaseRepositoryImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.MapRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.MapRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<MapRepository> { MapRepositoryImpl(get()) }
    single<FirebaseRepository> { FirebaseRepositoryImpl(get()) }

}