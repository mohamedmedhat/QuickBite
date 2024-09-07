package com.xapp.quickbit.data.source.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Area(
    @SerializedName("strArea")
    val strArea: String
) : Parcelable
