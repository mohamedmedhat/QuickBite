package com.xapp.quickbit.data.source.remote.network.restapi

data class productsItem(
    val country: String,
    val description: String,
    val difficulty: Int,
    val headline: String,
    val id: String,
    val image: String,
    val name: String,
)