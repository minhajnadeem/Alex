package com.example.datingapp.auth.verification

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import com.example.datingapp.Constants
import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentVerificationBinding
import com.example.datingapp.utils.MyPermissions
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class VerificationFragment : Fragment() {

    private val RC_CAMERA: Int = Constants.RC_CAMERA
    private val RC_GALLERY = Constants.RC_GALLERY

    lateinit var binding: FragmentVerificationBinding
    private lateinit var myPermissions: MyPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myPermissions = MyPermissions(activity as Activity)
        binding.verificationViewPager.adapter = ViewPagerAdapter(this, requireContext())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_CAMERA && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap

        }
        navigateToNext()
    }

    private fun navigateToNext() {
        val directions =
            VerificationFragmentDirections.actionVerificationFragmentToBlockFragment()
        findNavController().navigate(directions)
    }

    fun cameraIntent() {
        if (!myPermissions.hasCameraPermission()) {
            return
        }
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, RC_CAMERA)
            }
        }
    }

    fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            RC_GALLERY
        )
    }

    fun uploadPhoto() {
        navigateToNext()
    }
}
