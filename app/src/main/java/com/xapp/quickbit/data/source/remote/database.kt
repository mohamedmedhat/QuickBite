@file:OptIn(InternalCoroutinesApi::class)

package com.xapp.quickbit.data.source.remote

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.xapp.quickbit.data.source.remote.network.restapi.ColorDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [Colors::class], version = 1)
abstract class database:RoomDatabase(){
    abstract fun colorDao():ColorDao
    companion object{
        @Volatile
        private var Insatance:database?=null
        fun getInstance(context: Unit):database{
            return (Insatance?: synchronized(this){
                Insatance?: Room.databaseBuilder(
                    context.applicationContext,
                    database::class.java,"color"

                )
                    .fallbackToDestructiveMigrationFrom()
                    .build()
                    .also { Insatance=it }
            })
        }
    }
}