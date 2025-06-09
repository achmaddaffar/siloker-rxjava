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

    fun putEmployerId(id: Long) {
        preferences.edit {
            putLong(KEY_EMPLOYER_ID, id)
        }
    }

    fun getEmployerId(): Long = preferences.getLong(KEY_EMPLOYER_ID, -1)

    fun putJobSeekerId(id: Long) {
        preferences.edit {
            putLong(KEY_JOB_SEEKER_ID, id)
        }
    }

    fun getJobSeekerId(): Long = preferences.getLong(KEY_JOB_SEEKER_ID, -1)

    companion object {
        private const val KEY_TOKEN = "key_token"
        private const val KEY_EMPLOYER_ID = "key_employer_id"
        private const val KEY_JOB_SEEKER_ID = "key_job_seeker_id"
    }
}