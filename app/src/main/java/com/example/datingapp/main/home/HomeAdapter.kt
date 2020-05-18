package com.example.datingapp.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.R

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView
                >(R.id.imageView)
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
        return 4
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (position){
            0-> holder.imageView.setImageResource(R.drawable.img1)
            1-> holder.imageView.setImageResource(R.drawable.img2)
            2-> holder.imageView.setImageResource(R.drawable.img3)
            3-> holder.imageView.setImageResource(R.drawable.img4)
        }
    }
}