package com.example.datingapp.networking

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SuccessResponse(val success: Boolean, val message: String)

data class AuthResponse(
    val success: Boolean = true,
    val message: String,
    val profile: ProfileResponse
)

data class ProfileResponse(
    val id: Int,
    @SerializedName("username") val userName: String,
    val email: String,
    val verified: Boolean,
    @SerializedName("date_of_birth") val dob: Long,
    @SerializedName("is_complete") val isComplete: Boolean,
    @SerializedName("auth_token") var authToken: String,
    @SerializedName("profile_pic") val profilePic: String,
    @SerializedName("status_value") val statusValue: Int,
    val address: Address,
    @SerializedName("about_me") val aboutMe: String,
    val age: String,
    @SerializedName("hide_age") val isHideAge: Boolean,
    @SerializedName("hide_distance") val isHideDistance: Boolean

) : Serializable

data class Address(
    val address: String?,
    val street: String?,
    val city: String?,
    val state: String?,
    val zip: String?,
    val lat: Double?,
    val long: Double?
) : Serializable

data class UploadImagesResponse(val success: Boolean)

data class HomeResponse(val success: Boolean, val profiles: List<ProfileResponse>)

data class LikedListResponse(@SerializedName("matched_list") val likedList: List<ProfileResponse>)

/*Notification api response*/
data class NotificationsResponse(
    val success: Boolean,
    val notifications: List<Notifications>
)

data class Notifications(val text: String, val type: Int)

/*Update Profile Api Response*/
data class UpdateProfileResponse(
    val success: Boolean,
    val message: String,
    @SerializedName("profile_detail") var profile: ProfileResponse
)

/*Get Chat List*/
data class MatchedListResponse(@SerializedName("matched_list") val profiles: List<ProfileResponse>)
