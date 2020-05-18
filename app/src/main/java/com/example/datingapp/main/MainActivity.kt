package com.example.datingapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityMainBinding
import com.example.datingapp.databinding.FragmentHomeBinding
import com.example.datingapp.utils.MyPreferences
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var myPreferences: MyPreferences

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreferences = MyPreferences(this)
        myPreferences.setFirstStart(true)
        LoginManager.getInstance().logOut()
        FirebaseAuth.getInstance().signOut()

        init()
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
}
