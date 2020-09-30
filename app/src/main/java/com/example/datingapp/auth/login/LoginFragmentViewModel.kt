package com.example.datingapp.auth.login

import androidx.lifecycle.ViewModel
import com.example.datingapp.utils.MyPreferences

class LoginFragmentViewModel : ViewModel() {

    private val loginFragmentModel = LoginFragmentModel()
    private lateinit var myPreferences: MyPreferences

}