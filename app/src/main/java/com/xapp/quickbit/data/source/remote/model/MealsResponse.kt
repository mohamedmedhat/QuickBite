package com.xapp.quickbit.data.source.remote.model

import com.xapp.quickbit.data.source.local.entity.Meal

data class MealsResponse(

    val meals: List<Meal>
)
