package com.example.datingapp.editprofile

import com.example.datingapp.networking.AlexWebserviceHelper
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.UpdateProfileBody
import com.example.datingapp.networking.UpdateProfileResponse
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class EditProfileFragmentModel : AlexWebserviceHelper() {

    fun updateMyProfile(
        authToken: String,
        dob: Any = JSONObject.NULL,
        displayName: Any = JSONObject.NULL,
        aboutMe: Any = JSONObject.NULL,
        age: Any = JSONObject.NULL,
        listener: ApiListener<UpdateProfileResponse>
    ) {
        val updateProfileBody = UpdateProfileBody(
            authToken = authToken,
            dob = dob,
            displayName = displayName,
            aboutMe = aboutMe,
            age = age
        )
        updateProfile(updateProfileBody, listener)
    }
}