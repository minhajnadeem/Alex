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
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.fragment_settings.*


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
        setAgeSlider()
        setDistanceSlider()
    }

    private fun setDistanceSlider() {
        //val distanceRangeSeekbar = binding.rangeSeekbar2
        slider2.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                myPreferences.radius = slider.value.toInt()
            }
        })

        slider2.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            binding.tvDistanceRange.text = "Distance ${slider.value.toInt()} Kms"
        }
    }

    private fun setAgeSlider() {
        val ageRangeSlider = binding.rangeSlider
       /* ageRangeSeekbar.setMinStartValue(myPreferences.minAge.toFloat())
        ageRangeSeekbar.setMaxStartValue(myPreferences.maxAge.toFloat())
        ageRangeSeekbar.setGap(5f)
        ageRangeSeekbar.apply()
        ageRangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            binding.tvAgeRange.text = "Age $minValue-$maxValue"
        }

        ageRangeSlider.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            // Toast.makeText(activity, "$minValue : $maxValue", Toast.LENGTH_SHORT).show()
            myPreferences.minAge = minValue.toInt()
            myPreferences.maxAge = maxValue.toInt()
        }*/

        range_slider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
                myPreferences.minAge = slider.values[0].toInt()
                myPreferences.maxAge = slider.values[1].toInt()
            }
        })

        range_slider.addOnChangeListener { rangeSlider, value, fromUser ->
            // Responds to when slider's value is changed
           // Toast.makeText(activity, "Changed...", Toast.LENGTH_SHORT).show()
            binding.tvAgeRange.text = "Age ${rangeSlider.values[0].toInt()}-${rangeSlider.values[1].toInt()}"
        }
    }
}