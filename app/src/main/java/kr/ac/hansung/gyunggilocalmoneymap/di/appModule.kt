package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.ui.map.MapViewModel
import kr.ac.hansung.gyunggilocalmoneymap.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MapViewModel(get()) }
    viewModel { SplashViewModel(get())}
}