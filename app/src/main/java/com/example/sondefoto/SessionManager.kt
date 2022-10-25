package com.example.sondefoto

import android.content.Context
import android.content.SharedPreferences

const val SHARED_PREF_NAME = "SONDEFOR_MAILING_APP"

const val FIRST_NAME_KEY = "first_name"
const val LAST_NAME_KEY = "last_name"
const val NUM_CHANTIER_KEY = "num_chantier"
const val PRODUCTION_DATE_KEY = "production_date"


class SessionManager(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveString(key: String, data: String) {
        sharedPreferencesEditor.putString(key, data)
        sharedPreferencesEditor.commit()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

}