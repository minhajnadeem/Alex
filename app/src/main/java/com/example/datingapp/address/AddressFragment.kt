package com.example.datingapp.address

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.datingapp.Constants
import com.example.datingapp.databinding.FragmentAddressBinding
import com.example.datingapp.databinding.FragmentEditProfileBinding
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.ProfileResponse
import com.example.datingapp.networking.UpdateProfileResponse
import com.example.datingapp.utils.MyPreferences

class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private lateinit var myPreferences: MyPreferences
    /*private var profileResponse: ProfileResponse? = null
    private lateinit var editProfileFragmentModel: EditProfileFragmentModel*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myPreferences = MyPreferences(context)
        /*profileResponse = myPreferences.profile
        editProfileFragmentModel = EditProfileFragmentModel()*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        binding.tvTitle.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0;
                val DRAWABLE_TOP = 1;
                val DRAWABLE_RIGHT = 2;
                val DRAWABLE_BOTTOM = 3;

                if (event != null) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (event.getRawX() >= (binding.tvTitle.getRight() - binding.tvTitle.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
                                .width())
                        ) {
                            updateAdress()
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        /*profileResponse?.apply {
            binding.etDisplayName.setText(userName)
            binding.etAboutMe.setText(aboutMe)
            binding.etAge.setText(age)

        }*/
    }

    private fun updateAdress() {
        Toast.makeText(requireContext(), "Updated Adress", Toast.LENGTH_LONG).show()
        findNavController().popBackStack()
        /*val listener = object : ApiListener<UpdateProfileResponse> {
            override fun onSuccess(body: UpdateProfileResponse?) {
                if (body != null) {
                    if (body.success) {
                        Toast.makeText(requireContext(), body.message, Toast.LENGTH_LONG).show()
                        body.profile.authToken = myPreferences.getAuthToken()
                        myPreferences.profile = body.profile
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            Constants.STR_UNKNOWN_ERROR,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }

            override fun onFailure(error: Throwable) {
                error.printStackTrace()
            }
        }*/

       /* editProfileFragmentModel.updateMyProfile(
            authToken = myPreferences.getAuthToken(),
            displayName = binding.etDisplayName.text.toString(),
            aboutMe = binding.etAboutMe.text.toString(),
            age = binding.etAge.text.toString(),
            listener = listener
        )*/
    }
}