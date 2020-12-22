package com.example.datingapp.main.chat.onechat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.R

class ChatAdapter(list: ArrayList<MessageModel>, senderID: Int, receiverID: Int) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    private val messageList = list
    private val senderId = senderID
    private val receiverId = receiverID

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textViewSender = itemView.findViewById<TextView>(R.id.textView5)
        private val textViewReceiver = itemView.findViewById<TextView>(R.id.textView3)

        public fun setData(message: MessageModel, senderId: Int, receiverId: Int){
            if (message.sender!!.equals(senderId)){
                textViewSender.visibility=View.VISIBLE
                textViewReceiver.visibility=View.GONE
                textViewSender.text = message.text
            }else{
                textViewReceiver.visibility=View.VISIBLE
                textViewSender.visibility=View.GONE
                textViewReceiver.text = message.text
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(
            inflater.inflate(
                R.layout.row_layout_chat,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(messageList.get(position),senderId,receiverId)
    }
}