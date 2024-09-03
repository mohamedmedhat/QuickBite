package com.xapp.quickbit.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 0,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "email")
    val email: String?,

    @ColumnInfo(name = "image")
    val image: String? = null,

    @ColumnInfo(name = "cover")
    val cover: String? = null
) : Parcelable
