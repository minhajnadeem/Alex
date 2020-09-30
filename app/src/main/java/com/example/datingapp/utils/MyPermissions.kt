package com.example.datingapp.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.datingapp.Constants

data class CameraPermissionRequestResult(val isGranted: Boolean)

/**
 * this class is responsible for handling app permissions
 * contains methods to check and request for each permissions
 * the result pass back trough EventBus
 * to receive permission request result, subscribe to event
 *
 * from activity call fun onRequestPermissionResult to handle result
 */
class MyPermissions(val activity: Activity) {

    //camera permission
    private val rcCameraPermission = Constants.RC_PERMISSION_CAMERA
    private val STR_PERMISSION_CAMERA = android.Manifest.permission.CAMERA
    private val cameraPermission = arrayOf(STR_PERMISSION_CAMERA)

    //read external storage
    private val rcExternalStoragePermission = Constants.RC_PERMISSION_STORAGE
    private val strPermissionStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val storagePermission = arrayOf(strPermissionStorage)

    fun hasCameraPermission(shouldRequestPermission: Boolean = true): Boolean {
        if (hasPermission(STR_PERMISSION_CAMERA)) {
            return true
        } else if (shouldRequestPermission) {
            ActivityCompat.requestPermissions(activity, cameraPermission, rcCameraPermission)
        }
        return false
    }

    fun hasStoragePermission(shouldRequestPermission: Boolean = true): Boolean {
        if (hasPermission(strPermissionStorage)) {
            return true
        } else if (shouldRequestPermission) {
            ActivityCompat.requestPermissions(
                activity,
                storagePermission,
                rcExternalStoragePermission
            )
        }
        return false
    }

    /**
     * return true if app has specified permission
     * otherwise false
     */
    private fun hasPermission(strPermission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            strPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == rcCameraPermission) {
            if (isPermissionGranted(permissions, grantResults)) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        } else if (requestCode == rcExternalStoragePermission) {
            if (isPermissionGranted(permissions, grantResults)) {

            } else {

            }
        }
    }

    /**
     * return true if specified permissions are granted
     */
    private fun isPermissionGranted(
        permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        return grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }
}