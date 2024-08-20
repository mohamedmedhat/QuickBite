package com.xapp.quickbit.data.source.remote

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="colors")
data class Colors(val id: Int, val hex: String, val name: String) {
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="hex") val hex: String = ""
    @ColumnInfo(name="name")val name: String = ""

}
