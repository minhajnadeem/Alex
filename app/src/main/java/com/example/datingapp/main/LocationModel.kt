package com.example.datingapp.main

import com.example.datingapp.networking.AlexWebserviceHelper
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.UpdateProfileBody
import com.example.datingapp.networking.UpdateProfileResponse
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class LocationModel (
    val latitude:Double,
    val longitude:Double
) {

}