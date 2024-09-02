package com.xapp.quickbit.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "my_recipes")
data class MyRecipesEntity(
    @PrimaryKey(autoGenerate = true)
    val mealId: Long = 0,

    @ColumnInfo(name = "name")
    val mealName: String,

    @ColumnInfo(name = "country")
    val mealCountry: String,

    @ColumnInfo(name = "category")
    val mealCategory: String,

    @ColumnInfo(name = "instruction")
    val mealInstruction: String,

    @ColumnInfo(name = "thumb")
    val mealThumb: String,

    @ColumnInfo(name = "youtube_link")
    val mealYoutubeLink: String?
) : Parcelable
