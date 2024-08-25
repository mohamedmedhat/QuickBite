package com.xapp.quickbit.viewModel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity

class FavouriteAdapter(private val items: List<MealInformationEntity>, private val onItemClick: (MealInformationEntity) -> Unit) : RecyclerView.Adapter<FavouriteAdapter.FavouriteAdapterViewHolder>() {

    class FavouriteAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardFavImage: ImageView = view.findViewById(R.id.img_fav_meal)
        val cardFavText: TextView = view.findViewById(R.id.tv_fav_meal_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_favourite_item, parent, false)
        return FavouriteAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteAdapterViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.mealThumb)
            .into(holder.cardFavImage)
        holder.cardFavText.text = item.mealName

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
