package com.xapp.quickbit.data.source.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.xapp.quickbit.data.source.local.dao.FavouriteDao
import com.xapp.quickbit.data.source.local.dao.MyRecipesDao
import com.xapp.quickbit.data.source.local.dao.UserDao
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity
import com.xapp.quickbit.data.source.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, MealInformationEntity::class, MyRecipesEntity::class],
    version = 12
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favouriteDao(): FavouriteDao

    abstract fun myRecipesDao(): MyRecipesDao

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
