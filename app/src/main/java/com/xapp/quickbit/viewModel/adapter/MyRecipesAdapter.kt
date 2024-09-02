package com.xapp.quickbit.viewModel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity

class MyRecipesAdapter(
    private val items: MutableList<MyRecipesEntity>,
    private val onItemClick: (MyRecipesEntity) -> Unit,
    private val onItemDelete: (MyRecipesEntity) -> Unit
) : RecyclerView.Adapter<MyRecipesAdapter.MyRecipesAdapterViewHolder>() {


    class MyRecipesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardLayout: ConstraintLayout = view.findViewById(R.id.CardLayout)
        val cardImage: ImageView = view.findViewById(R.id.iv_RecycleViewImage)
        val cardTitle: TextView = view.findViewById(R.id.iv_RecycleViewText)
        val cardCategory: TextView = view.findViewById(R.id.tv_categoryIconText)
        val cardLocation: TextView = view.findViewById(R.id.tv_locationIconText)
        val deleteBtn: FloatingActionButton = view.findViewById(R.id.fab_deleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipesAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.my_recipes_item, parent, false)
        return MyRecipesAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecipesAdapterViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.mealThumb)
            .placeholder(R.drawable.images_placeholder)
            .error(R.drawable.error_24px)
            .into(holder.cardImage)
        holder.cardTitle.text = item.mealName
        holder.cardCategory.text = item.mealCategory
        holder.cardLocation.text = item.mealCountry
        holder.cardLayout.setOnClickListener {
            onItemClick(item)
        }
        holder.deleteBtn.setOnClickListener {
            onItemDelete(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun removeItem(meal: MyRecipesEntity) {
        val position = items.indexOf(meal)
        if (position != -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }


}