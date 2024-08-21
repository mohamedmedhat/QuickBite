package com.xapp.quickbit.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.xapp.quickbit.data.source.local.entity.Colors

@Dao
interface ColorDao {
    @Query("SELECT * FROM COLORS")
    fun getAll(): Array<Colors>

    @Insert
    fun insert(vararg colors: Colors)

}