package kr.ac.hansung.localcurrency.di

import kr.ac.hansung.localcurrency.ui.detail.DetailViewModel
import kr.ac.hansung.localcurrency.ui.map.MapViewModel
import kr.ac.hansung.localcurrency.ui.map.cluster.ClusterViewModel
import kr.ac.hansung.localcurrency.ui.map.preview.PreviewViewModel
import kr.ac.hansung.localcurrency.ui.search.SearchViewModel
import kr.ac.hansung.localcurrency.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MapViewModel(get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { PreviewViewModel() }
    viewModel { SearchViewModel(get()) }
    viewModel { ClusterViewModel() }
    viewModel { DetailViewModel() }
}