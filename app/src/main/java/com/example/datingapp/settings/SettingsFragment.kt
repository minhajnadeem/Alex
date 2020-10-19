package com.example.datingapp.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentSettingsBinding
import com.example.datingapp.splash.SplashActivity
import com.example.datingapp.utils.MyPreferences


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var myPreferences: MyPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myPreferences = MyPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutLogout.setOnClickListener {
            myPreferences.clearPrefs()
            Intent(requireContext(), SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        }
        binding.layoutAddress.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_addressFragment)
        }
        val rangeSeekbar = binding.rangeSeekbar1
        rangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            binding.tvAgeRange.text = "Age $minValue-$maxValue"
        }

        rangeSeekbar.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
           // Toast.makeText(activity, "$minValue : $maxValue", Toast.LENGTH_SHORT).show()
        }
        val rangeSeekbarDistance = binding.rangeSeekbar2
        rangeSeekbarDistance.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            binding.tvDistanceSeekbar.text = "Distance $minValue-$maxValue"
        }

        rangeSeekbarDistance.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            //Toast.makeText(activity, "$minValue : $maxValue", Toast.LENGTH_SHORT).show()
        }
    }
}