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
import kotlin.math.ln

class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private lateinit var myPreferences: MyPreferences
    private var profileResponse: ProfileResponse? = null
    private lateinit var addressFragmentModel: AddressFragmentModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myPreferences = MyPreferences(context)
        profileResponse = myPreferences.profile
        addressFragmentModel = AddressFragmentModel()
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
                            updateAddress()
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        profileResponse?.apply {
            binding.etAddress.setText(address.address)
            binding.etStreet.setText(address.street)
            binding.etCity.setText(address.city)
            binding.etState.setText(address.state)
            binding.etZipCode.setText(address.zip)
        }
    }

    private fun updateAddress() {
        Toast.makeText(requireContext(), "Updating Address", Toast.LENGTH_LONG).show()
        //findNavController().popBackStack()
        val listener = object : ApiListener<UpdateProfileResponse> {
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
        }
        val lat = myPreferences.currentLocation?.latitude
        val lng = myPreferences.currentLocation?.longitude
        addressFragmentModel.updateMyProfile(
            authToken = myPreferences.getAuthToken(),
            address = binding.etAddress.text.toString(),
            street = binding.etStreet.text.toString(),
            city = binding.etCity.text.toString(),
            state = binding.etState.text.toString(),
            zip = binding.etZipCode.text.toString(),
            lat = lat,
            long = lng,
            listener = listener
        )
    }
}