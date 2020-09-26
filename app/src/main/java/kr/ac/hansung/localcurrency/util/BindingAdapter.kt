package kr.ac.hansung.localcurrency.util

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.reactivex.subjects.Subject
import kr.ac.hansung.localcurrency.R

@BindingAdapter("bind:setPhone")
fun TextView.setPhone(title: String?) {
    if (title.equals("")) {
        this.text = "전화번호 없음"
    } else {
        this.text = title
    }
}

@BindingAdapter("bind:setAddress")
fun TextView.setAddress(address: String?) {
    if (address.equals("")) {
        this.text = "주소정보 없음"
    } else {
        this.text = address
    }
}

@BindingAdapter("bind:setCategory")
fun TextView.setCategory(category: String?) {
    if (category.equals("")) {
        this.text = "분류정보 없음"
    } else {
        this.text = category
    }
}

@BindingAdapter("bind:buttonClick")
fun View.buttonClick(buttonClickSubject: Subject<Unit>) {
    setOnClickListener {
        buttonClickSubject.onNext(Unit)
    }
}

@BindingAdapter("bind:setBackground")
fun View.setBackground(telePhone: String) {
    if (telePhone.equals("")) {
        this.setBackgroundResource(R.drawable.background_gray_btn)
    } else {
        this.setBackgroundResource(R.drawable.background_color_btn)
    }
}

@BindingAdapter("bind:setBottomSheetState")
fun View.setBottomSheetState(state: Int) {
    BottomSheetBehavior.from(this).apply {
        setState(state)
    }
}

@BindingAdapter("bindToolbar")
fun bindToolbar(toolbar: Toolbar, activity: AppCompatActivity) {
    activity.simpleToolbar(toolbar)
}

fun AppCompatActivity.simpleToolbar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowTitleEnabled(false)
        setHomeAsUpIndicator(R.drawable.ic_clear)
    }
}