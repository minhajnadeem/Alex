package com.example.datingapp.networking

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class AuthBody(@SerializedName("auth_token") val token: String)

data class RegisterBody(
    val username: String,
    val password: String,
    val email: String,
    @SerializedName("date_of_birth") val dob: Long,
    @SerializedName("fcm_token") val fcmToken: String?
)

data class LoginBody(val username: String, val password: String)

data class UploadImagesBody(
    @SerializedName("auth_token") val authToken: String,
    val image: String //base64 image
)

data class FbLoginBody(@SerializedName("fb_token") val fbToken: String)

data class GoogleLoginBody(@SerializedName("google_token") val googleToken: String)

data class HomeBody(
    @SerializedName("auth_token") val authToken: String,
    @SerializedName("min_age") val minAge: Int,
    @SerializedName("max_age") val maxAge: Int,
    @SerializedName("radius") val radius: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("long") val long: Double
)

data class LikeBody(
    @SerializedName("liked_profile_id") val id: Int,
    @SerializedName("auth_token") val authToken: String
)

data class RegisterFcmBody(
    @SerializedName("fcm_token") val fcmToken: String,
    @SerializedName("auth_token") val
    authToken: String
)

data class UpdateProfileBody(
    @SerializedName("auth_token") val authToken: String,
    @SerializedName("date_of_birth") var dob: Any = JSONObject.NULL,
    @SerializedName("display_name") var displayName: Any = JSONObject.NULL,
    @SerializedName("about_me") var aboutMe: Any = JSONObject.NULL,
    @SerializedName("age") var age: Any = JSONObject.NULL,
    @SerializedName("address") var address: Any = JSONObject.NULL,
    @SerializedName("street") var street: Any = JSONObject.NULL,
    @SerializedName("city") var city: Any = JSONObject.NULL,
    @SerializedName("state") var state: Any = JSONObject.NULL,
    @SerializedName("zip") var zip: Any = JSONObject.NULL
)