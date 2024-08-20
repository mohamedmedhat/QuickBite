package com.xapp.quickbit.data.source.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.xapp.quickbit.data.source.local.dao.SimpleEntityDao
import com.xapp.quickbit.data.source.local.dao.UserDao
import com.xapp.quickbit.data.source.local.entity.SimpleEntity
import com.xapp.quickbit.data.source.local.entity.User

@Database(entities = [SimpleEntity::class, User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun simpleEntityDao(): SimpleEntityDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
