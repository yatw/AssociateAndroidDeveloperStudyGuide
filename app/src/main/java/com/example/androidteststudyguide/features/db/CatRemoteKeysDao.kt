package com.example.androidteststudyguide.features.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CatRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CatRemoteKeys>)

    @Query("SELECT * FROM cat_remote_keys WHERE catId = :catId")
    suspend fun remoteKeysRepoId(catId: String): CatRemoteKeys?

    @Query("DELETE FROM cat_remote_keys")
    suspend fun clearRemoteKeys()
}
