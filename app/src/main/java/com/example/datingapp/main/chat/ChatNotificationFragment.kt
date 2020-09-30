package com.example.datingapp.main.chat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentChatNotificationBinding
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.AuthBody
import com.example.datingapp.networking.MatchedListResponse
import com.example.datingapp.utils.MyPreferences

/**
 * A simple [Fragment] subclass.
 */
class ChatNotificationFragment : Fragment() {

    private lateinit var binding: FragmentChatNotificationBinding
    private lateinit var chatNotificationModel: ChatNotificationModel
    private lateinit var myPreferences: MyPreferences
    private lateinit var chatNotificationAdapter: ChatNotificationAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        chatNotificationModel = ChatNotificationModel()
        myPreferences = MyPreferences(context)
    }

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

        chatNotificationModel.exeChatListApi(AuthBody(myPreferences.getAuthToken()),
            object : ApiListener<MatchedListResponse> {
                override fun onSuccess(body: MatchedListResponse?) {
                    chatNotificationAdapter.setData(body!!.profiles)
                }

                override fun onFailure(error: Throwable) {
                    error.printStackTrace()
                }

            })

        chatNotificationAdapter = ChatNotificationAdapter(requireContext())
        binding.rvChatNotifications.adapter = chatNotificationAdapter

    }
}
