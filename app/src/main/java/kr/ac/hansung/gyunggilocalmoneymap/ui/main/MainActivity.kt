package kr.ac.hansung.gyunggilocalmoneymap.ui.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.GeoConstants
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.ui.map.MapFragment

class MainActivity : AppCompatActivity() {


    private val mapFragment = supportFragmentManager.findFragmentByTag(MapFragment.TAG) ?: MapFragment.newInstance()

    private val compositeDisposable = CompositeDisposable()
    private val backKeySubject = BehaviorSubject.createDefault<Long>(0L)


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, mapFragment, MapFragment.TAG)
            .commit()


    }

    override fun onStart() {
        super.onStart()
        bindViewModel()

    }

    fun bindViewModel() {
        backKeySubject
            .buffer(2, 1)
            .map { it[0] to it[1] }
            .subscribe {
                if(it.second - it.first < 2000L) {
                    super.onBackPressed()
                } else {
                    Toast.makeText(this, "앱을 종료하려면 한번 더 눌러주세요", Toast.LENGTH_SHORT).show()
                }
            }
            .addTo(compositeDisposable)
    }

    override fun onBackPressed() {

            val fragment = supportFragmentManager.findFragmentByTag(MapFragment.TAG) as MapFragment

            if(fragment.getBottomSheetState() == BottomSheetBehavior.STATE_EXPANDED) {
                fragment.closeBottomSheet()
            } else {
            backKeySubject.onNext(System.currentTimeMillis())
        }

    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

}
