package com.xapp.quickbit.data.source.remote.model.response

import com.google.gson.annotations.SerializedName
import com.xapp.quickbit.data.source.remote.model.Category

data class CategoryResponse(
    @SerializedName("categories")
    val categories: List<Category>
)
