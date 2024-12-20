package com.xapp.quickbit.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "meal_information")
data class MealInformationEntity(
    @PrimaryKey
    val mealId: String,

    @ColumnInfo("name")
    val mealName: String,

    @ColumnInfo("country")
    val mealCountry: String,

    @ColumnInfo("category")
    val mealCategory: String,

    @ColumnInfo("instruction")
    val mealInstruction: String,

    @ColumnInfo("thumb")
    val mealThumb: String,

    @ColumnInfo("youtube_link")
    val mealYoutubeLink: String?
) : Parcelable
