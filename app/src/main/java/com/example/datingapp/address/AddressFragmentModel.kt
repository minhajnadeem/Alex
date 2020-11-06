package com.example.datingapp.address

import com.example.datingapp.networking.AlexWebserviceHelper
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.UpdateProfileBody
import com.example.datingapp.networking.UpdateProfileResponse
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class AddressFragmentModel : AlexWebserviceHelper() {

    fun updateMyProfile(
        authToken: String,
        address: Any = JSONObject.NULL,
        street: Any = JSONObject.NULL,
        city: Any = JSONObject.NULL,
        state: Any = JSONObject.NULL,
        zip: Any = JSONObject.NULL,
        listener: ApiListener<UpdateProfileResponse>
    ) {
        val updateProfileBody = UpdateProfileBody(
            authToken = authToken,
            address = address,
            street = street,
            city = city,
            state = state,
            zip = zip
        )
        updateProfile(updateProfileBody, listener)
    }
}