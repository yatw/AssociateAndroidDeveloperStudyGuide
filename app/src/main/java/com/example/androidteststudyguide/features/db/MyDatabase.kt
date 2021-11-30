package com.example.androidteststudyguide.features.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Cat::class, CatRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao
    abstract fun catRemoteKeysDao(): CatRemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MyDatabase::class.java, "MyDatabase"
            )
                .build()
    }
}
