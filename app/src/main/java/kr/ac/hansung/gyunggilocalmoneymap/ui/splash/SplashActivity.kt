package kr.ac.hansung.gyunggilocalmoneymap.ui.splash

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_splash.*
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.databinding.ActivitySplashBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseActivity
import kr.ac.hansung.gyunggilocalmoneymap.ui.main.MainActivity
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {

    override val vm: SplashViewModel by inject()
    private val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserve()
    }

    private fun initView() {
        bindViewModel()
        lottie_progress.apply {
            setMaxProgress(1f)
        }.playAnimation()
    }

    private fun initObserve() {
        vm._loadingCompleteEvent.observe(this, Observer {
            Intent(this, MainActivity::class.java).apply {
                finish()
                startActivity(this)
            }
        })

        vm.pageLoading.observe(this, Observer {
            lottie_progress.progress = it
            tv_progress.text = String.format(getString(R.string.progress_text), 1)
        })
    }

    private fun bindViewModel() {
        vm.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                lottie_progress.progress = it
                tv_progress.text = String.format(getString(R.string.progress_text), 1)
            }.addTo(compositeDisposable)
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }
}