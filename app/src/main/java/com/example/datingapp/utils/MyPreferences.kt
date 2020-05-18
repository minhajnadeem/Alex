package com.example.datingapp.utils
import android.content.Context

class MyPreferences(context: Context) : BasePreferences(context) {

    companion object {
        val DEF_STRING_VALUE: String? = null
        val DEF_BOOL_VALUE: Boolean = false

        val KEY_AUTH_TOKEN = "key_auth_token"
        val KEY_IS_FIRST_START = "key_is_first_start"

    }

    fun setAuthToken(token: String) {
        putString(KEY_AUTH_TOKEN, token)
    }

    fun isUserLoggedIn(): Boolean = getString(KEY_AUTH_TOKEN, DEF_STRING_VALUE) != null

    fun isFirstStart(): Boolean = getBool(KEY_IS_FIRST_START, true)

    fun setFirstStart(value: Boolean) {
        putBool(KEY_IS_FIRST_START, value)
    }
}