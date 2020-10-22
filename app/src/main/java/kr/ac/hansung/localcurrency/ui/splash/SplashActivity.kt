package kr.ac.hansung.localcurrency.ui.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_splash.*
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.databinding.ActivitySplashBinding
import kr.ac.hansung.localcurrency.ui.main.MainActivity
import kr.ac.hansung.localcurrency.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    val vm: SplashViewModel by viewModel()
    private val animationProgressSubject = BehaviorSubject.createDefault(false)

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SplashActivity, R.layout.activity_splash)
        binding.apply {
            binding.setVariable(BR.vm, vm)
            binding.lifecycleOwner = this@SplashActivity
        }


        initView()
        bindViewModel()
        initObserve()
    }

    private fun initView() {

        binding.lottieSplash.apply {
            setMaxProgress(1f)
            addAnimatorListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        animationProgressSubject.onNext(true)
                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }
                }
            )
        }.playAnimation()


    }

    override fun onStart() {
        super.onStart()
        bindViewModel()
    }

    private fun initObserve() {
        vm.loadingSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it == 270) {
                    setLoadingText(it)
                    binding.lottieSplash.loop(false)
                }
            }, {

            })
            .addTo(vm.getCompositeDisposable())

        Observable.combineLatest(
            animationProgressSubject,
            vm.getLoadedDataCompleteSubject(),
            BiFunction { ani: Boolean, data: Boolean ->
                ani to data
            }
        ).subscribe { pair ->
            if (pair.first && pair.second) {
                Intent(this, MainActivity::class.java).apply {
                    finish()
                    startActivity(this)
                }
            }
        }.addTo(vm.getCompositeDisposable())

        vm.loading.observe(this, EventObserver(this@SplashActivity::setLoadingText))
    }

    private fun bindViewModel() {

    }

    private fun setLoadingText(num: Int) {
        if (num == 270) {
            vm.loadedDataCompletedSubject.onNext(true)
            tv_progress.text = String.format(getString(R.string.loading_complete_text, num))
        } else {
            tv_progress.text = String.format(getString(R.string.loading_complete_text, num))
        }
    }


    override fun onDestroy() {
        vm.unBindViewModel()
        super.onDestroy()
    }
}