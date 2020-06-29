package kr.ac.hansung.gyunggilocalmoneymap.ui.splash

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_splash.*
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.databinding.ActivitySplashBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.base.BaseActivity
import kr.ac.hansung.gyunggilocalmoneymap.ui.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity :
    BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {

    override val vm: SplashViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    private val animaitonProgressSubject = BehaviorSubject.createDefault(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        bindViewModel()
        initObserve()
    }

    private fun initView() {

        lottie_splash.apply {
            setMaxProgress(1f)
            addAnimatorListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        animaitonProgressSubject.onNext(true)
                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }
                }
            )
        }.playAnimation()
    }

    private fun initObserve() {

    }

    private fun bindViewModel() {
        vm.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(it==270) {
                    tv_progress.text = String.format(getString(R.string.loading_complete_text, it))

                } else {
                    tv_progress.text = String.format(getString(R.string.progress_text), it)
                }
            }.addTo(compositeDisposable)

        Observable.combineLatest(
            animaitonProgressSubject, vm.loadedDataCompleteSubject, BiFunction { ani:Boolean, data:Boolean ->
                ani to data
            }
        ).subscribe { pair ->
            if (pair.first&&pair.second) {
                Intent(this, MainActivity::class.java).apply {
                    finish()
                    startActivity(this)
                }
            }
        }.addTo(compositeDisposable)

    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }
}