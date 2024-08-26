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

class CategoryDetailsAdapter(
    private val items: MutableList<Meal>,
    private val onItemClick: (Meal) -> Unit
) : RecyclerView.Adapter<CategoryDetailsAdapter.CategoryDetailsAdapterViewHolder>() {


    class CategoryDetailsAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardLayout: ConstraintLayout = view.findViewById(R.id.CategoriesDetailsCardLayout)
        val cardImage: ImageView = view.findViewById(R.id.iv_CategoriesDetailsRecycleViewImage)
        val cardText: TextView = view.findViewById(R.id.iv_CategoriesDetailsRecycleViewText)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryDetailsAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_category_details_items, parent, false)
        return CategoryDetailsAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryDetailsAdapterViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.strMealThumb)
            .placeholder(R.drawable.images_placeholder)
            .error(R.drawable.error_24px)
            .into(holder.cardImage)
        holder.cardText.text = item.strMeal
        holder.cardLayout.setOnClickListener {
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