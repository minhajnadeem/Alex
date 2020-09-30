package com.example.datingapp.auth.verification

import android.graphics.Bitmap
import com.example.datingapp.networking.AlexWebserviceHelper
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.UploadImagesBody
import com.example.datingapp.networking.UploadImagesResponse
import com.example.datingapp.utils.MyPreferences
import com.example.datingapp.utils.toBase64

class VerificationFragmentModel : AlexWebserviceHelper() {

    fun exeVerificationApi(
        myPreferences: MyPreferences,
        imageBitmap: Bitmap,
        listener: ApiListener<UploadImagesResponse>
    ) {
        val base64 = "data:image/jpeg;base64," + imageBitmap.toBase64()
        val body = UploadImagesBody(myPreferences.getAuthToken(), base64)
        uploadImages(body, listener)
    }
}