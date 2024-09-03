package com.xapp.quickbit.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.xapp.quickbit.data.source.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserDataByEmail(email: String): UserEntity?
}
