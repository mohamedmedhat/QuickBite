package com.xapp.quickbit.data.source.remote.model.response

import com.google.gson.annotations.SerializedName
import com.xapp.quickbit.data.source.remote.model.Area

data class AreaResponse(
    @SerializedName("meals")
    val meals: List<Area>
)
