package com.xapp.quickbit.data.source.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.xapp.quickbit.data.source.local.dao.SimpleEntityDao
import com.xapp.quickbit.data.source.local.entity.SimpleEntity

@Database(entities = [SimpleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun simpleEntityDao(): SimpleEntityDao

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
