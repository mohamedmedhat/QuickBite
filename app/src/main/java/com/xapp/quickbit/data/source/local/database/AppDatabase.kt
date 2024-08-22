package com.xapp.quickbit.data.source.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.xapp.quickbit.data.source.local.dao.SimpleEntityDao
import com.xapp.quickbit.data.source.local.dao.UserDao
import com.xapp.quickbit.data.source.local.entity.SimpleEntity
import com.xapp.quickbit.data.source.local.entity.User
import com.xapp.quickbit.data.source.local.entity.Colors
import com.xapp.quickbit.data.source.local.dao.ColorDao
import com.xapp.quickbit.data.source.local.dao.MealsDao
import com.xapp.quickbit.data.source.local.entity.MealDB

@Database(entities = [SimpleEntity::class, User::class, Colors::class, MealDB::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun simpleEntityDao(): SimpleEntityDao
    abstract fun userDao(): UserDao
    abstract fun colorDao(): ColorDao
    abstract fun mealDao(): MealsDao

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
