package com.xapp.quickbit.data.source.local.entity

import androidx.room.Entity


@Entity(tableName = "favorites")
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)
