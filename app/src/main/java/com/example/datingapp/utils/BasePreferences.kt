package com.example.datingapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.datingapp.networking.AuthResponse
import com.google.gson.Gson

abstract class BasePreferences(context: Context) {
    private val name: String = "datingapp.Preferences"
    private val mode: Int = Context.MODE_PRIVATE
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(name, mode)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()


    fun putString(key: String, value: String?) {
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

    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun <T> getObject(key: String, classOfT: Class<T>): T? {
        val gson = Gson()
        val stringObj = getString(key, "")
        return if (stringObj != null) {
            if (stringObj.isEmpty()) {
                null
            } else {
                gson.fromJson(stringObj, classOfT)
            }
        } else {
            null
        }
    }

    fun <T> putObject(key: String, value: T) {
        val gson = Gson()
        val jsonObj = gson.toJson(value)
        putString(key, jsonObj)
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }
}