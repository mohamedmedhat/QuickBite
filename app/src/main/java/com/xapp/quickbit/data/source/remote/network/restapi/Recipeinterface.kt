package com.xapp.quickbit.data.source.remote.network.restapi

import retrofit2.http.GET

interface Recipeinterface {

    @GET("/ahmedeltaher/78e7f1b54d4b60af73b9802b38abf3cf")

    fun getData() : retrofit2.Call<List<productsItem>>


}