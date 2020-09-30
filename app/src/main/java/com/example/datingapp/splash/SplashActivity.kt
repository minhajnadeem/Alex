package com.example.datingapp.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64.encodeToString
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.Constants.Companion.DELAY_SPLASH
import com.example.datingapp.R
import com.example.datingapp.auth.AuthActivity
import com.example.datingapp.main.MainActivity
import com.example.datingapp.networking.AlexWebserviceHelper
import com.example.datingapp.utils.MyPreferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.concurrent.timerTask


class SplashActivity : AppCompatActivity() {

    private lateinit var myPreferences: MyPreferences
    private val timer: Timer
    private lateinit var timerTask: TimerTask

    init {
        timer = Timer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        myPreferences = MyPreferences(this)
        timerTask = timerTask {
            startNewActivity()
        }
        timer.schedule(timerTask, DELAY_SPLASH)

        getCurrentFcmToken()
    }

    private fun getCurrentFcmToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(SplashActivity::class.simpleName, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                myPreferences.fcmToken = token

                Log.d(SplashActivity::class.simpleName,token)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        timer.purge()
    }

    private fun startNewActivity() {
        if (myPreferences.isUserLoggedIn()) {
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        } else {
            Intent(this, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        }
    }

}
