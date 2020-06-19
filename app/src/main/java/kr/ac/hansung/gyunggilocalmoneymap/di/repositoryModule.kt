package kr.ac.hansung.gyunggilocalmoneymap.di


import kr.ac.hansung.gyunggilocalmoneymap.data.NaverMapRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.NaverMapRepositoryImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.OpenApiRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.OpenApiRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<OpenApiRepository> {
        OpenApiRepositoryImpl(
            get(),
            get()
        )
    }
    single<NaverMapRepository> { NaverMapRepositoryImpl(get()) }
}

