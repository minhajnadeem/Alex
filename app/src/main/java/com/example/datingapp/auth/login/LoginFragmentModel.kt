package com.example.datingapp.auth.login

import com.example.datingapp.networking.*

class LoginFragmentModel : AlexWebserviceHelper() {

    fun exeRegisterApi(
        userName: String,
        email: String,
        password: String,
        dob: Long,
        fcm: String?,
        registerListener: ApiListener<AuthResponse>
    ) {
        val body = RegisterBody(userName, password, email, dob,fcm)
        clientRegister(body, registerListener)
    }

    fun exeLoginApi(
        userName: String,
        password: String,
        registerListener: ApiListener<AuthResponse>
    ) {
        val body = LoginBody(userName, password)
        clientLogin(body, registerListener)
    }

    fun exeFbLoginApi(fbToken: String, registerListener: ApiListener<AuthResponse>) {
        val body = FbLoginBody(fbToken)
        clientFbLogin(body, registerListener)
    }

    fun exeGoogleLoginApi(googleToken: String, registerListener: ApiListener<AuthResponse>) {
        val body = GoogleLoginBody(googleToken)
        clientGoogleLogin(body, registerListener)
    }
}