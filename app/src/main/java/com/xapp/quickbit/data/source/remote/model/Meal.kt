package com.xapp.quickbit.data.source.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meal(
    @SerializedName("idMeal")
    val idMeal: String,

    @SerializedName("strMeal")
    val strMeal: String,

    @SerializedName("strMealThumb")
    val strMealThumb: String
) : Parcelable
