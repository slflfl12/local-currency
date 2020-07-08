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

fun String.splitPhoneNum(): String {

    if(this == "") {
        return ""
    }

    val split: List<String> = this.trim().split("-")

    if (split.size == 1) {
        return this
    }

    val builder = StringBuilder()
    for (value in split) {
        builder.append(value)
    }
    return builder.toString()
}


fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
