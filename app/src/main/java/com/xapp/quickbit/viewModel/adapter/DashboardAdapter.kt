package com.xapp.quickbit.viewModel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity

class DashboardAdapter(
    private val items: MutableList<MyRecipesEntity>,
    private val onItemClick: (MyRecipesEntity) -> Unit,
) : RecyclerView.Adapter<DashboardAdapter.DashboardAdapterViewHolder>() {

    class DashboardAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: ConstraintLayout = view.findViewById(R.id.CardLayout)
        val cardImage: ImageView = view.findViewById(R.id.iv_RecycleViewImage)
        val cardTitle: TextView = view.findViewById(R.id.iv_RecycleViewText)
        val cardLocation: TextView = view.findViewById(R.id.tv_locationIconText)
        val cardCategory: TextView = view.findViewById(R.id.tv_categoryIconText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item, parent, false)
        return DashboardAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardAdapterViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.mealThumb)
            .placeholder(R.drawable.images_placeholder)
            .error(R.drawable.error_24px)
            .into(holder.cardImage)
        holder.cardTitle.text = item.mealName
        holder.cardCategory.text = item.mealCategory
        holder.cardLocation.text = item.mealCountry
        holder.card.setOnClickListener {
            onItemClick(item)
        }

        when (position) {
            0 -> holder.card.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.gold
                )
            )

            1 -> holder.card.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.silver
                )
            )

            2 -> holder.card.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.bronze
                )
            )

            else -> holder.card.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }
    }


    override fun getItemCount(): Int = items.size
}