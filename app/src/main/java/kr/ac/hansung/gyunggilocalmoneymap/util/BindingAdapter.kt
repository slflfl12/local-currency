package kr.ac.hansung.gyunggilocalmoneymap.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.reactivex.subjects.Subject

@BindingAdapter("bind:setPhone")
fun TextView.setPhone(title: String?) {
    if(title.equals("")) {
        this.text = "전화번호 없음"
    } else {
        this.text = title
    }
}

@BindingAdapter("bind:setAddress")
fun TextView.setAddress(address: String?) {
    if(address.equals("")) {
        this.text = "주소정보 없음"
    } else {
        this.text = address
    }
}

@BindingAdapter("bind:setCategory")
fun TextView.setCategory(category: String?) {
    if(category.equals("")) {
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