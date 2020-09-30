package com.example.datingapp.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.R
import com.example.datingapp.networking.ProfileResponse

class HomeAdapter(val profile: List<ProfileResponse>) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(
            inflater.inflate(
                R.layout.row_layout_home,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return profile.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val profile = profile[position]
        holder.tvName.text = profile.userName
        val drawable = if (profile.verified) {
            R.drawable.ic_verified
        } else {
            R.drawable.ic_unverified
        }
        holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }

    fun getProfileByPosition(position: Int): ProfileResponse {
        return profile[position]
    }
}