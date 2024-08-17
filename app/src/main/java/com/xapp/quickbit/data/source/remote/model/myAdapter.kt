package com.xapp.quickbit.data.source.remote.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.xapp.quickbit.R

class myAdapter(private val imageList:ArrayList<recycleView>):
    RecyclerView.Adapter<myAdapter.myViewHolder>() {
private lateinit var mylistener:onItemClickListener
interface onItemClickListener{

    fun onItemClick(position: Int)
}
fun setOnItemClickListener(listener:onItemClickListener){

    mylistener=listener
}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.listitem,parent,false)
        return myViewHolder(itemView,mylistener)


    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentItem=imageList[position]
        holder.image.setImageResource(currentItem.image)
        holder.heading.text=currentItem.heading
    }

    class myViewHolder(itemView: View,listener:onItemClickListener):RecyclerView.ViewHolder(itemView){

        val image:ShapeableImageView=itemView.findViewById(R.id.mainImage)
        val heading:TextView=itemView.findViewById(R.id.head)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}