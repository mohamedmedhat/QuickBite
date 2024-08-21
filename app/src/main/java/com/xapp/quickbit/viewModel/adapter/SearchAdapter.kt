package com.xapp.quickbit.viewModel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.RecycleView

class SearchAdapter(private val imageList: ArrayList<RecycleView>) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {
    private lateinit var mylistener: OnItemClickListener

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listitem, parent, false)
        return MyViewHolder(itemView, mylistener)


    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = imageList[position]
        holder.image.setImageResource(currentItem.image)
        holder.heading.text = currentItem.heading
    }

    class MyViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        val image: ShapeableImageView = itemView.findViewById(R.id.mainImage)
        val heading: TextView = itemView.findViewById(R.id.head)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

}