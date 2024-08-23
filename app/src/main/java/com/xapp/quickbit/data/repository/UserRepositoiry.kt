package com.xapp.quickbit.data.repository

import com.xapp.quickbit.data.source.local.dao.UserDao
import com.xapp.quickbit.data.source.local.entity.UserEntity

class UserRepository(private val userDao: UserDao) {
    suspend fun createUser(user: UserEntity) {
        userDao.addUser(user)
    }
}