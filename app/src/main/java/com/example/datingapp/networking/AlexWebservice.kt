package com.example.datingapp.networking

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AlexWebservice {

    companion object {
        const val REGISTER = "client/register"
        const val LOGIN = "client/login"
        const val UPLOAD_IMAGES = "client/upload_images"
        const val FB_LOGIN = "client/facebook_login"
        const val GOOGLE_LOGIN = "client/google_login"
        const val HOME_DATA = "client/home_data"
        const val LIKE = "client/like"
        const val DIS_LIKE = "client/dis_like"
        const val GET_LIKED_LIST = "client/get_liked_list"
        const val REGISTER_FCM_TOKEN = "client/register_fcm_token"
        const val NOTIFICATION = "client/get_user_notification"
        const val UPDATE_PROFILE = "client/update_profile"
        const val MATCHED_LIST = "client/get_matched_list"
    }

    @POST(REGISTER)
    fun clientRegister(@Body body: RegisterBody): Call<AuthResponse>

    @POST(LOGIN)
    fun clientLogin(@Body loginBody: LoginBody): Call<AuthResponse>

    @POST(UPLOAD_IMAGES)
    fun uploadImage(@Body uploadImagesBody: UploadImagesBody): Call<UploadImagesResponse>

    @POST(FB_LOGIN)
    fun fbLogin(@Body fbLoginBody: FbLoginBody): Call<AuthResponse>

    @POST(GOOGLE_LOGIN)
    fun googleLogin(@Body googleLoginBody: GoogleLoginBody): Call<AuthResponse>

    @POST(HOME_DATA)
    fun home(@Body homeBody: HomeBody): Call<HomeResponse>

    @POST(LIKE)
    fun like(@Body homeBody: LikeBody): Call<SuccessResponse>

    @POST(DIS_LIKE)
    fun disLike(@Body homeBody: LikeBody): Call<SuccessResponse>

    @POST(GET_LIKED_LIST)
    fun getLikedList(@Body body: AuthBody): Call<LikedListResponse>

    @POST(REGISTER_FCM_TOKEN)
    fun registerFcmToken(@Body body: RegisterFcmBody): Call<SuccessResponse>

    @POST(NOTIFICATION)
    fun getUserNotifications(@Body body: AuthBody): Call<NotificationsResponse>

    @POST(UPDATE_PROFILE)
    fun updateProfile(@Body body: UpdateProfileBody): Call<UpdateProfileResponse>

    @POST(MATCHED_LIST)
    fun getMatchedList(
        @Body body: AuthBody
    ): Call<MatchedListResponse>
}