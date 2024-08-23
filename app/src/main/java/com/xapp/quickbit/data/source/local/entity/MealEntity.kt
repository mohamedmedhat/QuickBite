package com.xapp.quickbit.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class MealEntity(
    @PrimaryKey
    val idMeal: String,

    @ColumnInfo("strMeal")
    val strMeal: String,

    @ColumnInfo("strMealThumb")
    val strMealThumb: String
)
