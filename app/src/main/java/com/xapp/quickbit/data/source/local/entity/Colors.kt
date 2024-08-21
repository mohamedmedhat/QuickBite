package com.xapp.quickbit.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Colors(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo("hex")
    val hex: String,

    @ColumnInfo("name")
    val name: String,
)