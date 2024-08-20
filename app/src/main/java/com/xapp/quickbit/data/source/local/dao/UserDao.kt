package com.xapp.quickbit.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.xapp.quickbit.data.source.local.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: User)
}