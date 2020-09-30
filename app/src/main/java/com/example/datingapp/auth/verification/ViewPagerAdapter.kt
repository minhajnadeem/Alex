package com.example.datingapp.auth.verification

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.datingapp.R

class ViewPagerAdapter(
    private val verificationFragment: VerificationFragment,
    val context: Context
) :
    PagerAdapter() {

    //layout 3
    lateinit var btnTakePhoto: Button
    lateinit var btnGallary: Button
    lateinit var btnUploadPhoto: Button
    lateinit var ivVerificationPhoto: ImageView

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val resId = when (position) {
            0 -> R.layout.layout_verification_page1
            1 -> R.layout.layout_verification_page2
            2 -> R.layout.layout_verification_page3
            else -> R.layout.layout_verification_page1
        }
        val layout = inflater.inflate(resId, container, false)
        container.addView(layout)

        when (position) {
            2 -> {

                ivVerificationPhoto = layout.findViewById(R.id.ivVerificationPhoto)

                btnTakePhoto = layout.findViewById(R.id.btnTakePhoto)
                btnTakePhoto.setOnClickListener {
                    verificationFragment.cameraIntent()
                }
                btnGallary = layout.findViewById(R.id.btnGallary)
                btnGallary.setOnClickListener {
                    verificationFragment.galleryIntent()

                }
                btnUploadPhoto = layout.findViewById(R.id.btnUploadPhoto)
                btnUploadPhoto.setOnClickListener {
                    verificationFragment.uploadPhoto()

                }
            }
        }
        return layout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return 3
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)

    }

    fun setVerificationPhoto(bitmap: Bitmap) {
        ivVerificationPhoto.setImageBitmap(bitmap)
    }
}