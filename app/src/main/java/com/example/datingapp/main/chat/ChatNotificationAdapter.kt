package com.example.datingapp.main.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.networking.ProfileResponse
import de.hdodenhof.circleimageview.CircleImageView

class ChatNotificationAdapter(val context: Context) :
    RecyclerView.Adapter<ChatNotificationAdapter.MyViewHolder>() {

    private var list = listOf<ProfileResponse>()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivChatProfile = view.findViewById<CircleImageView>(R.id.ivChatProfile)
        val tvChatProfileName = view.findViewById<TextView>(R.id.tvChatProfileName)
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
    }

    fun setData(body: List<ProfileResponse>) {
        list = body
        notifyDataSetChanged()
    }
}