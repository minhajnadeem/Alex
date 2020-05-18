package com.example.datingapp

import android.app.Activity
import android.app.Application
import android.os.Bundle

class MyApplication : Application(),Application.ActivityLifecycleCallbacks {

    companion object{
        val applicationContext = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onActivityPaused(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityStarted(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityResumed(activity: Activity) {
        TODO("Not yet implemented")
    }
}