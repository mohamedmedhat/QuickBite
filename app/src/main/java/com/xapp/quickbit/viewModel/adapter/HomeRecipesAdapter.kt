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
import com.xapp.quickbit.data.source.remote.model.MealDetail

class HomeRecipesAdapter(
    private val items: MutableList<MealDetail>,
    private val onItemClick: (MealDetail) -> Unit
) : RecyclerView.Adapter<HomeRecipesAdapter.RecipeMealsHomeViewHolder>() {

    class RecipeMealsHomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: ConstraintLayout = view.findViewById(R.id.CardLayout)
        val cardImage: ImageView = view.findViewById(R.id.iv_RecycleViewImage)
        val cardTitle: TextView = view.findViewById(R.id.iv_RecycleViewText)
        val cardCategory: TextView = view.findViewById(R.id.tv_categoryIconText)
        val cardLocation: TextView = view.findViewById(R.id.tv_locationIconText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeMealsHomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_recipes_item, parent, false)
        return RecipeMealsHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeMealsHomeViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.strMealThumb)
            .into(holder.cardImage)
        holder.cardTitle.text = item.strMeal
        holder.cardCategory.text = item.strCategory
        holder.cardLocation.text = item.strArea
        holder.card.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<MealDetail>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}