package com.example.datingapp.auth.verification

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.datingapp.Constants
import com.example.datingapp.databinding.FragmentVerificationBinding
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.UploadImagesResponse
import com.example.datingapp.utils.AlexDialogsHelper
import com.example.datingapp.utils.MyPermissions
import com.example.datingapp.utils.MyPreferences


/**
 * A simple [Fragment] subclass.
 */
class VerificationFragment : Fragment() {

    private val RC_CAMERA: Int = Constants.RC_CAMERA
    private val RC_GALLERY = Constants.RC_GALLERY

    private lateinit var activity: Activity
    lateinit var binding: FragmentVerificationBinding
    private lateinit var myPermissions: MyPermissions
    private lateinit var myPreferences: MyPreferences
    private val model = VerificationFragmentModel()
    private lateinit var listener: ApiListener<UploadImagesResponse>
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var uploadPhoto: Bitmap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myPreferences = MyPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as Activity
        myPermissions = MyPermissions(activity)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.INVISIBLE
        viewPagerAdapter = ViewPagerAdapter(this, requireContext())
        binding.verificationViewPager.adapter = viewPagerAdapter

        listener = object : ApiListener<UploadImagesResponse> {
            override fun onSuccess(body: UploadImagesResponse?) {
                binding.progressBar.visibility = View.INVISIBLE
                navigateToNext()

            }

            override fun onFailure(error: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                AlexDialogsHelper().showToastMessage(requireContext(), error.message)

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_CAMERA && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            uploadPhoto = imageBitmap
            viewPagerAdapter.setVerificationPhoto(imageBitmap)
        } else if (requestCode == RC_GALLERY && resultCode == RESULT_OK) {
            val selectedImage: Uri? = data!!.data
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)

            val cursor: Cursor = activity.getContentResolver().query(
                selectedImage!!,
                filePathColumn, null, null, null
            )!!
            cursor.moveToFirst()

            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
            val picturePath: String = cursor.getString(columnIndex)
            cursor.close()

            uploadPhoto = BitmapFactory.decodeFile(picturePath)

        }
    }


    private fun navigateToNext() {
        val directions =
            VerificationFragmentDirections.actionVerificationFragmentToHomeActivity()
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
        if (!myPermissions.hasStoragePermission()) {
            return
        }
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            RC_GALLERY
        )
    }

    fun uploadPhoto() {
        binding.progressBar.visibility = View.VISIBLE
        model.exeVerificationApi(myPreferences, uploadPhoto, listener)

    }
}
