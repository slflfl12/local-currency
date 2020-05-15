package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.ui.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MapViewModel()}
}