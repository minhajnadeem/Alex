package com.example.datingapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

abstract class BasePreferences(context: Context) {
    private val name: String = "datingapp.Preferences"
    private val mode: Int = Context.MODE_PRIVATE
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(name, mode)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()


    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun getString(key: String, defValue: String?): String? {
        return sharedPreferences.getString(key, defValue)
    }

    fun putBool(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }
}