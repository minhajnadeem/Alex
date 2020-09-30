package com.example.datingapp.utils.notifications

import com.example.datingapp.networking.AlexWebserviceHelper
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.RegisterFcmBody
import com.example.datingapp.networking.SuccessResponse

class NotificationsModel : AlexWebserviceHelper() {

    fun registerFcmToken(
        fcmToken: String,
        authToken: String,
        listener: ApiListener<SuccessResponse>
    ) {
        val body = RegisterFcmBody(fcmToken, authToken)
        registerFcmToken(body, listener)
    }
}