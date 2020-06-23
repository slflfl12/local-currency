package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.domain.usecase.GetMyLocationPlacesUseCase
import org.koin.dsl.module

val usecaseModule = module {

    single { GetMyLocationPlacesUseCase(get(), get()) }
}