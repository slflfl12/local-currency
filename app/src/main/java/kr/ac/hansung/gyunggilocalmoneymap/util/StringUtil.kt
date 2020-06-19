package kr.ac.hansung.gyunggilocalmoneymap.util

fun String.siguntoSi(): String {

    if (this.contains(" ")) {
        return this.split(" ")[0]
    } else {
        return this
    }
}