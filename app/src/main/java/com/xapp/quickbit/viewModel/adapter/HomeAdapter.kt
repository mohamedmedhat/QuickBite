package com.xapp.quickbit.viewModel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xapp.quickbit.data.source.local.entity.Meal
import com.xapp.quickbit.databinding.MostPopularCardBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MostPopularMealViewHolder>() {
    private var mealsList: List<Meal> = ArrayList()
    private lateinit var onItemClick: OnItemClick
    private lateinit var onLongItemClick: OnLongItemClick

    fun setMealList(mealsList: List<Meal>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    fun setOnClickListener(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setOnLongClickListener(onLongItemClick: OnLongItemClick) {
        this.onLongItemClick = onLongItemClick
    }

    class MostPopularMealViewHolder(val binding: MostPopularCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostPopularMealViewHolder {
        return MostPopularMealViewHolder(
            MostPopularCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MostPopularMealViewHolder, position: Int) {
        val meal = mealsList[position]

        holder.binding.apply {

            Glide.with(holder.itemView)
                .load(meal.strMealThumb)
                .into(imgPopularMeal)


            tvMealName.text = meal.strMeal
        }

        holder.itemView.setOnClickListener {
            onItemClick.onItemClick(meal)
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick.onItemLongClick(meal)
            true
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }


    interface OnItemClick {
        fun onItemClick(meal: Meal)
    }


    interface OnLongItemClick {
        fun onItemLongClick(meal: Meal)
    }
}
