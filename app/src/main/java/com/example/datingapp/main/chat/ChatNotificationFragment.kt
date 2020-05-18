package com.example.datingapp.main.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentChatNotificationBinding

/**
 * A simple [Fragment] subclass.
 */
class ChatNotificationFragment : Fragment() {

    private lateinit var binding: FragmentChatNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rowLayoutChat.setOnClickListener {
            findNavController().navigate(R.id.chatFragment)
        }
        binding.rowLayoutChat2.setOnClickListener {
            findNavController().navigate(R.id.chatFragment)
        }
    }
}
