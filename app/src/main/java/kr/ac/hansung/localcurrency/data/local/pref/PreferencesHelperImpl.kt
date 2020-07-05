package kr.ac.hansung.localcurrency.data.local.pref

import android.content.Context
import androidx.core.content.edit


class PreferencesHelperImpl(
    context: Context
) :PreferencesHelper {

    private val preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    override var appVersion: String?
        get() = preferences.getString(KEY_APP_VERSION, "notLoaded")
        @Synchronized
        set(value) {
            preferences.edit(false) {
                putString(KEY_APP_VERSION, value)
            }
        }

    override var loadedData: String?
        get() = preferences.getString(KEY_LOADED_DATA, "1")
        @Synchronized
        set(value) {
            preferences.edit(false) {
                putString(KEY_LOADED_DATA, value)
            }
        }


    companion object {
        const val PREF_FILE_NAME = "kr.ac.hansung.localcurrency.data.local.pref"

        const val KEY_APP_VERSION = "keyAppVersion"
        const val KEY_LOADED_DATA = "keyLoadedData"
    }
}