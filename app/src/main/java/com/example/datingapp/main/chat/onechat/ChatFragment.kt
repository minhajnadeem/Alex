package com.example.datingapp.main.chat.onechat

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datingapp.MyApplication.Companion.applicationContext

import com.example.datingapp.databinding.FragmentChatBinding
import com.example.datingapp.main.chat.onechat.ChatAdapter
import com.example.datingapp.videochat.CallActivity
import com.example.quickbloxvideocall.WebRtcSessionManager
import com.quickblox.chat.QBChatService
import com.quickblox.users.model.QBUser
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCTypes

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvChat.adapter =
            ChatAdapter()

        binding.ivBtnVideoCall.setOnClickListener {
            startCall(true)
        }
    }

    private fun startCall(isVideoCall: Boolean) {
        val conferenceType =
            QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO

        val qbrtcClient = QBRTCClient.getInstance(context)
        val newQbRtcSession =
            qbrtcClient.createNewSessionWithOpponents(
//                listOf(119089320),  //testaccount4 id
                listOf(119089281),  //testaccount3 id
                conferenceType
            )
        WebRtcSessionManager.setCurrentSession(newQbRtcSession)

        CallActivity.start(requireContext(), false)
    }

}
