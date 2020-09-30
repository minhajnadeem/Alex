package com.example.datingapp.main.chat

import com.example.datingapp.networking.AlexWebserviceHelper
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.AuthBody
import com.example.datingapp.networking.MatchedListResponse

class ChatNotificationModel : AlexWebserviceHelper() {

    fun exeChatListApi(authBody: AuthBody,apiListener: ApiListener<MatchedListResponse>){
        getChatList(authBody,apiListener)
    }
}