package com.xapp.quickbit.data.repository

import com.xapp.quickbit.data.source.local.dao.UserDao
import com.xapp.quickbit.data.source.local.database.AppDatabase
import com.xapp.quickbit.data.source.local.entity.User

class UserRepository(private val userDao: UserDao) {
    suspend fun createUser(user: User) {
        userDao.addUser(user)
    }
}