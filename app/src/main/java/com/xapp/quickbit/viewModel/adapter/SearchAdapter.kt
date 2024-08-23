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

class SearchAdapter(
    private val items: MutableList<MealDetail>,
    private val onItemClick: (MealDetail) -> Unit
) :
    RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder>() {


    class SearchAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: ConstraintLayout = view.findViewById(R.id.SearchCardLayout)
        val cardImage: ImageView = view.findViewById(R.id.iv_SearchRecycleViewImage)
        val cardTitle: TextView = view.findViewById(R.id.iv_SearchRecycleViewText)
        val cardCategory: TextView = view.findViewById(R.id.tv_SearchcategoryIconText)
        val cardLocation: TextView = view.findViewById(R.id.tv_SearchlocationIconText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_list_item, parent, false)
        return SearchAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapterViewHolder, position: Int) {
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