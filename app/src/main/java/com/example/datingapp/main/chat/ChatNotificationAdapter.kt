package com.example.datingapp.main.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.networking.ProfileResponse
import com.example.datingapp.utils.MyPreferences
import de.hdodenhof.circleimageview.CircleImageView

class ChatNotificationAdapter(val context: Context) :
    RecyclerView.Adapter<ChatNotificationAdapter.MyViewHolder>() {

    private var list = listOf<ProfileResponse>()
    private val mContext = context

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivChatProfile = view.findViewById<CircleImageView>(R.id.ivChatProfile)
        val tvChatProfileName = view.findViewById<TextView>(R.id.tvChatProfileName)
        val rootLayoutChat = view.findViewById<ConstraintLayout>(R.id.root_layout_chat_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val from = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout_chat_notficiation, parent, false)
        return MyViewHolder(from)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val profile = list[position]
        Glide.with(context).load(profile.profilePic).into(holder.ivChatProfile)
        holder.tvChatProfileName.text = profile.userName
        holder.rootLayoutChat.setOnClickListener {
           // Toast.makeText(mContext, " id "+profile.id, Toast.LENGTH_SHORT).show()
            val preferences = MyPreferences(mContext)
            preferences.receiverID = profile.id
            it.findNavController().navigate(R.id.action_chatNotificationFragment_to_chatFragment)
        }
    }

    fun setData(body: List<ProfileResponse>) {
        list = body
        notifyDataSetChanged()
    }
}