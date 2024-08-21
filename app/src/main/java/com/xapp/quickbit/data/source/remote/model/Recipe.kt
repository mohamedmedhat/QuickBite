package com.xapp.quickbit.data.source.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import com.squareup.moshi.JsonClass

@Parcelize
@JsonClass(generateAdapter = true)
data class Recipe(
    @SerializedName("id")
    val id: Long = 0,
) : Parcelable
