package com.xapp.quickbit.data.source.remote.model.response

import com.google.gson.annotations.SerializedName
import com.xapp.quickbit.data.source.remote.model.Meal

data class MealsResponse(
    @SerializedName("meals")
    val meals: List<Meal>
)