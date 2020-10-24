package kr.ac.hansung.localcurrency.ui.splash

import android.Manifest
import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import com.tedpark.tedpermission.rx2.TedRx2Permission
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
                TedRx2Permission.with(this)
                    .setRationaleTitle("경고")
                    .setRationaleMessage("위치 권한 허가를 하셔야 정상적으로 서비스를 이용하실 수 있습니다. 위치 권한 허용을 해주세요 [Setting] > [Permission]") // "we need permission for read contact and find your location"
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .request()
                    .subscribe({
                        if(it.isGranted) {
                            Intent(this, MainActivity::class.java).apply {
                                finish()
                                startActivity(this)
                            }
                        } else {
                            Toast.makeText(this,
                                "권한이 거부되었습니다. 정상적으로 앱이 동작하지 않을 수 있습니다.\n" + it.getDeniedPermissions().toString(), Toast.LENGTH_SHORT)
                                .show();
                        }
                    }, {

                    }).addTo(vm.getCompositeDisposable())


            }
        }.addTo(vm.getCompositeDisposable())

        vm.loading.observe(this, EventObserver(this@SplashActivity::setLoadingText))
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