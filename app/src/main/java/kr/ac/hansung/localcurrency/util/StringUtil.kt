package kr.ac.hansung.localcurrency.util

import android.content.Context
import android.widget.Toast

fun String.splitFirst(): String {

    if (this.contains(" ")) {
        return this.split(" ")[0]
    } else {
        return this
    }
}

fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}