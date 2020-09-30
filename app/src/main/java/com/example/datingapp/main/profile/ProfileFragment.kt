package com.example.datingapp.main.profile

import android.R.attr
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider.getUriForFile
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.datingapp.Constants.Companion.RC_CAMERA
import com.example.datingapp.Constants.Companion.RC_GALLERY
import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentProfileBinding
import com.example.datingapp.inappbilling.MyBillingClient
import com.example.datingapp.networking.ProfileResponse
import com.example.datingapp.utils.AlexDialogsHelper
import com.example.datingapp.utils.ChooserOnClick
import com.example.datingapp.utils.MyPermissions
import com.example.datingapp.utils.MyPreferences
import com.facebook.FacebookSdk.getCacheDir
import com.yalantis.ucrop.UCrop
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var fileName: String
    private lateinit var activity: Activity
    private lateinit var binding: FragmentProfileBinding
    private lateinit var myBillingClient: MyBillingClient
    private lateinit var myPreferences: MyPreferences
    private var myProfile: ProfileResponse? = null
    private lateinit var myPermissions: MyPermissions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myPreferences = MyPreferences(context)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as Activity
        myPermissions = MyPermissions(activity)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myProfile = myPreferences.profile

        myProfile?.also {
            Glide.with(this).load(it.profilePic).into(binding.ivProfile)
            binding.tvProfileName.text = it.userName
            if (it.address.street == null) {
                binding.tvProfileAddress.text = "Add your address"
            } else {
                binding.tvProfileAddress.text = it.address.street
            }
        }
        myBillingClient = MyBillingClient(requireActivity())
        binding.btnTindoGold.setOnClickListener {
            myBillingClient.launchPurchaseFlow()
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        binding.btnProfilePicture.setOnClickListener {
            showProfilePictureChooser()
        }
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_CAMERA && resultCode == Activity.RESULT_OK) {
            cropImage(getCacheImagePath(fileName))
        } else if (requestCode == RC_GALLERY && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri = data!!.data!!
            cropImage(imageUri)
        } else if (requestCode == UCrop.REQUEST_CROP) {
            handleUCropResult(data);
        }
    }

    private fun showProfilePictureChooser() {
        AlexDialogsHelper().showChooser(requireContext(), object : ChooserOnClick {
            override fun onClickCamera() {
                openCameraIntent()
            }

            override fun onClickGallery() {
                openGalleryIntent()
            }

        })
    }

    fun openCameraIntent() {
        if (!myPermissions.hasCameraPermission()) {
            return
        }
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            fileName = "${System.currentTimeMillis()}.jpg";
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName))
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, RC_CAMERA)
            }
        }
    }

    fun openGalleryIntent() {
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

    private fun getCacheImagePath(fileName: String): Uri {
        val path = File(activity.externalCacheDir, "camera")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        return getUriForFile(
            requireContext(),
            requireContext().packageName.toString() + ".provider",
            image
        )
    }

    private fun cropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(
            File(
                getCacheDir(),
                queryName(requireContext().contentResolver, sourceUri)
            )
        )
        val options = UCrop.Options()
        options.withAspectRatio(1f, 1f)
        options.withMaxResultSize(1000, 1000)
        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(requireActivity())
    }

    private fun queryName(resolver: ContentResolver, uri: Uri): String {
        val returnCursor: Cursor = resolver.query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    private fun handleUCropResult(data: Intent?) {
        if (data == null) {
            return
        }
        val resultUri = UCrop.getOutput(data)
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, resultUri)
        Glide.with(requireContext()).asBitmap().load(bitmap).into(binding.ivProfile)
    }
}
