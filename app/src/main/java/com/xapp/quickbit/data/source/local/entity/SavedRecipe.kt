package com.xapp.quickbit.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_recipe")
data class SavedRecipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
)
