package com.example.androidteststudyguide.features.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Cat>)

    @Query(
        "SELECT * FROM cat"
    )
    fun getCats(): PagingSource<Int, Cat>

    @Query("DELETE FROM cat")
    suspend fun clearCats()
}
