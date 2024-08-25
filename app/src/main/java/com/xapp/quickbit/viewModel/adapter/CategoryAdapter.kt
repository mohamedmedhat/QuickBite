package com.xapp.quickbit.viewModel.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.Category


class CategoryAdapter(
    private val items: MutableList<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryAdapterViewHolder>() {


    class CategoryAdapterViewHolder(view: View) : ViewHolder(view) {
        val card: ConstraintLayout = view.findViewById(R.id.CategoryCardLayout)
        val image: ImageView = view.findViewById(R.id.iv_RecycleViewCategoryImage)
        val name: TextView = view.findViewById(R.id.tv_RecycleViewCategoryText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_category_item, parent, false)
        return CategoryAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapterViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.strCategoryThumb)
            .into(holder.image)
        holder.name.text = item.strCategory
        holder.card.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Category>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

}