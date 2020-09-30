package com.example.datingapp.main.notifications

import com.example.datingapp.networking.AlexWebserviceHelper
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.AuthBody
import com.example.datingapp.networking.NotificationsResponse

class NotificationFragmentModel : AlexWebserviceHelper() {

    fun exeGetNotificationApi(authToken: String, apiListener: ApiListener<NotificationsResponse>) {
        val body = AuthBody(authToken)
        getUserNotifications(body, listener = apiListener)
    }
}