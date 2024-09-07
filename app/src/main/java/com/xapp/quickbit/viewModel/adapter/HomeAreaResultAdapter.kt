package com.xapp.quickbit.viewModel.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.Meal

class HomeAreaResultAdapter(
    private val items: MutableList<Meal>,
    private val onItemClick: (Meal) -> Unit
) : RecyclerView.Adapter<HomeAreaResultAdapter.HomeAreaResultAdapterViewHolder>() {

    class HomeAreaResultAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: ConstraintLayout = view.findViewById(R.id.CardLayout)
        val title: TextView = view.findViewById(R.id.iv_RecycleViewText)
        val cardImage: ImageView = view.findViewById(R.id.iv_areaResultImage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeAreaResultAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.area_result_item, parent, false)
        return HomeAreaResultAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeAreaResultAdapterViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.strMealThumb)
            .placeholder(R.drawable.images_placeholder)
            .error(R.drawable.error_24px)
            .into(holder.cardImage)
        holder.title.text = item.strMeal
        holder.card.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Meal>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}