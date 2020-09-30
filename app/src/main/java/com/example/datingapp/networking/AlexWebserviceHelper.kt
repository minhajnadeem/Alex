package com.example.datingapp.networking

abstract class AlexWebserviceHelper {

    private val webservice = BaseWebservice.getApiEndpointImpl()

    fun clientRegister(body: RegisterBody, apiListener: ApiListener<AuthResponse>) {
        val call = webservice.clientRegister(body)
        BaseWebservice.executeApi(call, apiListener)
    }

    fun clientLogin(body: LoginBody, apiListener: ApiListener<AuthResponse>) {
        val call = webservice.clientLogin(body)
        BaseWebservice.executeApi(call, apiListener)
    }

    fun uploadImages(body: UploadImagesBody, apiListener: ApiListener<UploadImagesResponse>) {
        val call = webservice.uploadImage(body)
        BaseWebservice.executeApi(call, apiListener)
    }

    fun clientFbLogin(body: FbLoginBody, apiListener: ApiListener<AuthResponse>) {
        val call = webservice.fbLogin(body)
        BaseWebservice.executeApi(call, apiListener)
    }

    fun clientGoogleLogin(body: GoogleLoginBody, apiListener: ApiListener<AuthResponse>) {
        val call = webservice.googleLogin(body)
        BaseWebservice.executeApi(call, apiListener)
    }

    fun homeDate(body: HomeBody, listener: ApiListener<HomeResponse>) {
        val call = webservice.home(body)
        BaseWebservice.executeApi(call, listener)
    }

    fun like(body: LikeBody, listener: ApiListener<SuccessResponse>) {
        val call = webservice.like((body))
        BaseWebservice.executeApi(call, listener)
    }

    fun disLike(body: LikeBody, listener: ApiListener<SuccessResponse>) {
        val call = webservice.disLike((body))
        BaseWebservice.executeApi(call, listener)
    }

    fun getLikedProfiles(authBody: AuthBody, listener: ApiListener<LikedListResponse>) {
        val call = webservice.getLikedList(authBody)
        BaseWebservice.executeApi(call, listener)
    }

    fun registerFcmToken(registerFcmBody: RegisterFcmBody, listener: ApiListener<SuccessResponse>) {
        val call = webservice.registerFcmToken(registerFcmBody)
        BaseWebservice.executeApi(call, listener)
    }

    fun getUserNotifications(body: AuthBody, listener: ApiListener<NotificationsResponse>) {
        val userNotifications = webservice.getUserNotifications(body)
        BaseWebservice.executeApi(userNotifications, listener)
    }

    fun updateProfile(body: UpdateProfileBody, listener: ApiListener<UpdateProfileResponse>) {
        val updateProfile = webservice.updateProfile(body)
        BaseWebservice.executeApi(updateProfile, listener)
    }

    fun getChatList(body: AuthBody, listener: ApiListener<MatchedListResponse>) {
        val chatList = webservice.getMatchedList(body)
        BaseWebservice.executeApi(chatList,listener)
    }
}