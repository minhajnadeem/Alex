package com.example.datingapp.utils

import android.content.Context
import android.location.Location
import com.example.datingapp.main.LocationModel
import com.example.datingapp.networking.AuthResponse
import com.example.datingapp.networking.ProfileResponse

class MyPreferences(context: Context) : BasePreferences(context) {

    companion object {
        val DEF_STRING_VALUE: String? = null
        val DEF_BOOL_VALUE: Boolean = false
        val DEF_AUTH_TOKEN_VALUE: String = ""
        val DEF_FCM_TOKEN_VALUE: String = ""


        val KEY_AUTH_TOKEN = "key_auth_token"
        val KEY_IS_FIRST_START = "key_is_first_start"
        val KEY_PROFILE = "key_profile"
        val KEY_FCM_TOKEN = "key_fcm_token"
        val KEY_CURRENT_LOCATION = "key_current_location"
        val KEY_MIN_AGE = "key_minimum_age"
        val KEY_MAX_AGE = "key_maximum_age"
        val KEY_RADIUS = "key_radius"
        val KEY_RCEIVER_ID = "key_receiver_id"

    }

    var currentLocation: LocationModel?
        get() {
            return getObject(KEY_CURRENT_LOCATION, LocationModel::class.java)
        }
        set(value) {
            putObject(KEY_CURRENT_LOCATION, value)
        }

    var profile: ProfileResponse?
        get() {
            return getObject(KEY_PROFILE, ProfileResponse::class.java)
        }
        set(value) {
            putObject(KEY_PROFILE, value)
        }

    var receiverID:Int
        get() {return getInt(KEY_RCEIVER_ID,0)}
        set(value) {putInt(KEY_RCEIVER_ID,value)}

    var radius:Int
        get() {return getInt(KEY_RADIUS,5)}
        set(value) {putInt(KEY_RADIUS,value)}

    var minAge:Int
        get() {return getInt(KEY_MIN_AGE,20)}
        set(value) {putInt(KEY_MIN_AGE,value)}

    var maxAge:Int
        get() {return getInt(KEY_MAX_AGE,50)}
        set(value) {putInt(KEY_MAX_AGE,value)}

    fun getAuthToken(): String {
        val authResponse = getObject(KEY_PROFILE, ProfileResponse::class.java)
        return authResponse?.authToken ?: DEF_AUTH_TOKEN_VALUE
    }

    fun isUserLoggedIn(): Boolean = getObject(KEY_PROFILE, ProfileResponse::class.java) != null

    var fcmToken: String?
        get() {
            return getString(KEY_FCM_TOKEN, DEF_FCM_TOKEN_VALUE)
        }
        set(value) {
            putString(KEY_FCM_TOKEN, value)
        }

    fun clearPrefs(){
        clear()
    }
}