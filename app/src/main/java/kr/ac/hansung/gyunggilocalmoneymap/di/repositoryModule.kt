package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.data.FirebaseRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.FirebaseRepositoryImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.LocalMapRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.LocalMapRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<LocalMapRepository> { LocalMapRepositoryImpl(get()) }
    single<FirebaseRepository> { FirebaseRepositoryImpl(get()) }

}