package com.example.datingapp.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.datingapp.MyApplication
import com.example.datingapp.MyApplication.Companion.ACCOUNT_KEY
import com.example.datingapp.MyApplication.Companion.AUTH_KEY
import com.example.datingapp.MyApplication.Companion.AUTH_SECRET
import com.example.datingapp.MyApplication.Companion.DEFAULT_USER_PASSWORD
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityMainBinding
import com.example.datingapp.utils.MyPreferences
import com.example.datingapp.videochat.BaseActivity
import com.example.quickbloxvideocall.LoginService
import com.example.quickbloxvideocall.PERMISSIONS
import com.example.quickbloxvideocall.PermissionsActivity
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.quickblox.auth.session.QBSettings
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser

class MainActivity : BaseActivity() {

    lateinit var myPreferences: MyPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    //videoChat
    var currentUser: QBUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreferences = MyPreferences(this)
        LoginManager.getInstance().logOut()
        FirebaseAuth.getInstance().signOut()

        init()

        //videoChat
        initSdk()
        if (checkPermissions(PERMISSIONS)) {
            startPermissionsActivity(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
        if (navHostFragment != null) {
            val childFragments = navHostFragment.childFragmentManager.fragments
            childFragments.forEach { fragment ->
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun init() {
        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.menu.getItem(2).isChecked = true
        val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { menuItme ->
                val destinationId = when (menuItme.itemId) {
                    R.id.menu_chat -> R.id.chatNotificationFragment
                    R.id.menu_profile -> R.id.profileFragment
                    R.id.menu_home -> R.id.homeFragment
                    R.id.menu_likes -> R.id.likesFragment
                    R.id.menu_notification -> R.id.notificationsFragment
                    else -> {
                        return@OnNavigationItemSelectedListener true
                    }
                }
                findNavController(R.id.navHostFragment).navigate(destinationId)
                return@OnNavigationItemSelectedListener true
            }
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }

    private fun startPermissionsActivity(checkOnlyAudio: Boolean) {
        PermissionsActivity.startForResult(this, checkOnlyAudio, PERMISSIONS)
    }

    private fun initSdk() {
        QBSettings.getInstance()
            .init(applicationContext, MyApplication.APPLICATION_ID, AUTH_KEY, AUTH_SECRET)
        QBSettings.getInstance().accountKey = ACCOUNT_KEY
        loginUser()
    }

    private fun loginUser() {
        val user = QBUser()
        user.login = "testaccount4"
        user.fullName = "Test Account4"
        user.password = DEFAULT_USER_PASSWORD

        QBUsers.signIn(user).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(user: QBUser?, args: Bundle?) {
                Log.d("quickbloxvideocall.TAG", user.toString())
                currentUser = user
//                connectToChatServer()
                startLoginService()
            }

            override fun onError(error: QBResponseException?) {
                Log.d("quickbloxvideocall.TAG", error.toString())

            }
        })
    }

    private fun startLoginService() {
        if (currentUser != null) {
            currentUser!!.password = DEFAULT_USER_PASSWORD
            LoginService.start(this, currentUser!!)
        }

    }
}
