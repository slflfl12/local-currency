package kr.ac.hansung.localcurrency.di


import kr.ac.hansung.localcurrency.data.repository.NaverMapRepository
import kr.ac.hansung.localcurrency.data.repository.NaverMapRepositoryImpl
import kr.ac.hansung.localcurrency.data.repository.OpenApiRepository
import kr.ac.hansung.localcurrency.data.repository.OpenApiRepositoryImpl
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

