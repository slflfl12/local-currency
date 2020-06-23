package kr.ac.hansung.gyunggilocalmoneymap.di


import kr.ac.hansung.gyunggilocalmoneymap.data.repository.NaverMapRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.repository.NaverMapRepositoryImpl
import kr.ac.hansung.gyunggilocalmoneymap.data.repository.OpenApiRepository
import kr.ac.hansung.gyunggilocalmoneymap.data.repository.OpenApiRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<OpenApiRepository> {
        OpenApiRepositoryImpl(
            get(),
            get()
        )
    }
    single<NaverMapRepository> {
        NaverMapRepositoryImpl(
            get()
        )
    }
}

