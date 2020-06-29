package kr.ac.hansung.gyunggilocalmoneymap.ui.search

import android.os.Bundle
import com.naver.maps.map.MapView
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.databinding.ActivitySearchBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>(R.layout.activity_search) {

    override val vm: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initView() {

    }

    fun initObserve() {

    }

    fun bindViewModel() {

    }


}