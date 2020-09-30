package com.example.datingapp.main.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.R
import com.example.datingapp.networking.Notifications

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>() {

    private var list: List<Notifications> = emptyList()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNotificationText = itemView.findViewById<TextView>(R.id.tvNotificationText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_layout_notifications, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.tvNotificationText.text = list[position].text
    }

    fun setData(list: List<Notifications>) {
        this.list = list
        notifyDataSetChanged()
    }
}