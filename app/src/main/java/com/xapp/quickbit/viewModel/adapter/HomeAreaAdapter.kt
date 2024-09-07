package com.xapp.quickbit.viewModel.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.Area

class HomeAreaAdapter(
    private val items: MutableList<Area>,
    private val onItemClick: (Area) -> Unit
) : RecyclerView.Adapter<HomeAreaAdapter.HomeAreaAdapterViewHolder>() {

    class HomeAreaAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: ConstraintLayout = view.findViewById(R.id.cardLayout)
        val cardText: TextView = view.findViewById(R.id.tv_area_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAreaAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.area_items, parent, false)
        return HomeAreaAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeAreaAdapterViewHolder, position: Int) {
        val item = items[position]
        holder.cardText.text = item.strArea
        holder.card.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Area>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}