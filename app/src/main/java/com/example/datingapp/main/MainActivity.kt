package com.example.datingapp.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
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
    //location
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
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
        setUpLocationListener()
    }

    private fun fetchCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showPermissionAlert()
                return
            }
        }
        mFusedLocationClient!!.requestLocationUpdates(getLocationRequest(), mLocationCallback, null /* Looper */)
    }

    private fun getLocationRequest(): LocationRequest? {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 900
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return locationRequest
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        when (requestCode) {
            123 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showPermissionAlert()
                } else {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fetchCurrentLocation()
                    }
                }
            }
        }
    }

    private fun showPermissionAlert() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION), 123)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION), 123)
            }
        }
    }

    private fun setUpLocationListener() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val location = LocationModel(locationResult!!.lastLocation.latitude,locationResult.lastLocation.longitude)
                myPreferences.currentLocation = location
                val loc=myPreferences.currentLocation
                stopLocationUpdates()
            }
        }
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

    override fun onResume() {
        super.onResume()
        fetchCurrentLocation()
    }
}
