package com.example.datingapp.main.chat.onechat

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.datingapp.databinding.FragmentChatBinding
import com.example.datingapp.networking.ProfileResponse
import com.example.datingapp.utils.MyPreferences
import com.example.datingapp.videochat.CallActivity
import com.example.quickbloxvideocall.WebRtcSessionManager
import com.google.firebase.database.*
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCTypes


/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private var mDatabase: DatabaseReference? = null
    private var receiverID:Int=0
    private var senderID:Int=0
    private var profile: ProfileResponse?=null
    private val chatId:String = "9_8"
    private val messageList = arrayListOf<MessageModel>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)
        mDatabase = FirebaseDatabase.getInstance().reference
        val preferences = MyPreferences(activity as Context)
        receiverID = preferences.receiverID
        profile = preferences.profile
        senderID = profile!!.id
        binding.textView6.text=profile!!.userName
        fetchMessages();
        return binding.root
    }

    private fun fetchMessages() {
        mDatabase!!.child("messages").child(chatId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        for (teacherSnapshot in snapshot.children) {
                            Log.e("Teacher Key : ", teacherSnapshot.key)
                            val message: MessageModel = teacherSnapshot.getValue(MessageModel::class.java)!!
                            messageList.add(message)
                        }
                        if (messageList.isEmpty()) {
                           // Toast.makeText(activity, "Currently No Messages Available", Toast.LENGTH_SHORT).show()
                        } else {
                            chatAdapter = ChatAdapter(messageList,senderID,receiverID)
                            binding.rvChat.adapter = chatAdapter
                            chatAdapter.notifyDataSetChanged()

                        }
                        // Toast.makeText(MyTeachersActivity.this, "Teachers "+teachersList.size()+" "+teachersList.get(0).getName(), Toast.LENGTH_SHORT).show();
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(activity, "Error : " + error.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      /*  binding.rvChat.adapter =
            ChatAdapter()
*/
        binding.ivBtnVideoCall.setOnClickListener {
            startCall(true)
        }
        binding.imageButton10.setOnClickListener {
            sendMessageFirebase()
        }

    }

    private fun sendMessageFirebase() {
        //Toast.makeText(activity, "Sender $senderID Receiver $receiverID", Toast.LENGTH_SHORT).show()
        //String post_id = mDatabase.child("students").push().getKey();
        //val message = MessageModel(senderID,receiverID,binding.editText.text.toString())
        val message = MessageModel()
        message.sender=senderID
        message.receiver=receiverID
        message.text=binding.editText.text.toString()
        binding.editText.setText("")
        binding.editText.clearFocus()
        hideKeyboardFrom(activity as Context,binding.root)
       // Toast.makeText(activity, " "+message.text, Toast.LENGTH_SHORT).show();
        val child_id = mDatabase!!.child("messages").push().getKey();
        mDatabase!!.child("messages").child(chatId).child(child_id.toString()).setValue(message)
            .addOnCompleteListener {
               // Toast.makeText(activity, "Sent", Toast.LENGTH_SHORT).show();
            }.addOnFailureListener { e ->
            Toast.makeText(
                activity,
                "Not sent..." + e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
