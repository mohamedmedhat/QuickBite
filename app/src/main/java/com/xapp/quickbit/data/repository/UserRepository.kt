package com.xapp.quickbit.data.repository

import android.content.Context
import com.xapp.quickbit.data.source.local.database.AppDatabase
import com.xapp.quickbit.data.source.local.entity.UserEntity

class UserRepository(context: Context) {
    private val userDao = AppDatabase.getInstance(context).userDao()

    suspend fun createUser(user: UserEntity) {
        userDao.addUser(user)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    suspend fun getUserDataByEmail(email: String): UserEntity? {
        return userDao.getUserDataByEmail(email)
    }
}
