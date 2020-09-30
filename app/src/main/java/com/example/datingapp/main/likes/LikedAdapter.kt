package com.example.datingapp.main.likes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.R
import com.example.datingapp.main.home.HomeAdapter
import com.example.datingapp.networking.LikedListResponse
import com.example.datingapp.networking.ProfileResponse
import kotlinx.android.synthetic.main.list_item_liked.view.*

class LikedAdapter(val list: List<ProfileResponse>) :
    RecyclerView.Adapter<LikedAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvProfileName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(
            inflater.inflate(
                R.layout.list_item_liked,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list.get(position)
        holder.tvName.text = model.userName
    }
}