package com.example.datingapp.main.home

import com.example.datingapp.networking.*

class HomeFragmentModel : AlexWebserviceHelper() {

    fun exeHomeDataApi(authToken: String, listener: ApiListener<HomeResponse>) {
        homeDate(HomeBody(authToken), listener)
    }

    fun exeLikeApi(likedId: Int, authToken: String, listener: ApiListener<SuccessResponse>) {
        like(LikeBody(likedId, authToken), listener)
    }

    fun exeDisLikeApi(likedId: Int, authToken: String, listener: ApiListener<SuccessResponse>) {
        disLike(LikeBody(likedId, authToken), listener)
    }
}