package com.xapp.quickbit.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("email")
    val email: String,

    @ColumnInfo("password")
    val password: String
)
