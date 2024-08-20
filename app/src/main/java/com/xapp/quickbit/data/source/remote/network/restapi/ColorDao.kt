package com.xapp.quickbit.data.source.remote.network.restapi

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.xapp.quickbit.data.source.remote.Colors

@Dao
interface ColorDao {
    @Query("SELECT * FROM COLORS")
    fun getAll():Array<Colors>
    @Query("SELECT * FROM COLORS WHERE name")
    fun getColorByName(name:String):LiveData<Colors>
    @Query("SELECT * FROM COLORS WHERE hex")
    fun getColorByHex(name:String):LiveData<Colors>
    @Insert
    fun insert(vararg colors: Colors)
    @Update
    fun update(vararg colors: Colors)
    @Delete
    fun delete(vararg colors: Colors)


}