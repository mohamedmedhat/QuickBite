package com.xapp.quickbit.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.xapp.quickbit.data.source.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: UserEntity)
}