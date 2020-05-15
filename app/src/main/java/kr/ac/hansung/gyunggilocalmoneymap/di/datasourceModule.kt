package kr.ac.hansung.gyunggilocalmoneymap.di

import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.LocalMapDataSource
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.source.LocalMapDataSourceImpl
import org.koin.dsl.module


val datasourceModule = module {

    single<LocalMapDataSource> { LocalMapDataSourceImpl(get()) }
}