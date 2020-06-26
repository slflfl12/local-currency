package kr.ac.hansung.gyunggilocalmoneymap.ui.search

import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.databinding.ActivitySearchBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>(R.layout.activity_search) {

    override val vm: SearchViewModel by viewModel()
}