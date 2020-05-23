package kr.ac.hansung.gyunggilocalmoneymap.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_splash.*
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.databinding.ActivitySplashBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseActivity
import kr.ac.hansung.gyunggilocalmoneymap.ui.main.MainActivity
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {

    override val vm: SplashViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserve()
        initView()
    }

    private fun initView() {
    }

    private fun initObserve() {
        vm._loadingCompleteEvent.observe(this, Observer {
            Intent(this, MainActivity::class.java).apply {
                finish()
                startActivity(this)
            }
        })
    }
}