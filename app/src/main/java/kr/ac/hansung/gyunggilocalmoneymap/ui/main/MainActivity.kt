package kr.ac.hansung.gyunggilocalmoneymap.ui.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.geometry.GeoConstants
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.ui.map.MapFragment

class MainActivity : AppCompatActivity() {


    private val mapFragment = supportFragmentManager.findFragmentByTag(MapFragment.TAG) ?: MapFragment.newInstance()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, mapFragment, MapFragment.TAG)
            .commit()


    }
}
