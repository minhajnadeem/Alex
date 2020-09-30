package com.example.datingapp.main.likes

import com.example.datingapp.networking.AlexWebserviceHelper
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.AuthBody
import com.example.datingapp.networking.LikedListResponse

class LikesFragmentModel : AlexWebserviceHelper() {

    fun getLikedList(authToken: String, listener: ApiListener<LikedListResponse>) {
        getLikedProfiles(AuthBody((authToken)), listener)
    }
}