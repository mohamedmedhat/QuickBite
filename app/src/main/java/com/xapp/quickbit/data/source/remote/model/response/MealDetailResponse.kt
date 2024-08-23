package com.xapp.quickbit.data.source.remote.model.response

import com.google.gson.annotations.SerializedName
import com.xapp.quickbit.data.source.remote.model.MealDetail

data class MealDetailResponse(
    @SerializedName("meals")
    val meals: List<MealDetail>
)
