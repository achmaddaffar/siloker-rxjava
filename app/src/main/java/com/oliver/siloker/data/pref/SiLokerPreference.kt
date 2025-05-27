package com.oliver.siloker.data.pref

import android.content.SharedPreferences
import androidx.core.content.edit

class SiLokerPreference(
    private val preferences: SharedPreferences
) {

    fun putToken(token: String?) {
        preferences.edit {
            putString(KEY_TOKEN, token)
        }
    }

    fun getToken(): String? = preferences.getString(KEY_TOKEN, null)

    companion object {
        private const val KEY_TOKEN = "key_token"
    }
}