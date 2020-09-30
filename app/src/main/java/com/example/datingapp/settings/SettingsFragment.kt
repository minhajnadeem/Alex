package com.example.datingapp.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.datingapp.auth.AuthActivity
import com.example.datingapp.databinding.FragmentSettingsBinding
import com.example.datingapp.main.MainActivity
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
    }
}