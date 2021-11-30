package com.example.androidteststudyguide.features.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat_remote_keys")
data class CatRemoteKeys(
    @PrimaryKey val catId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
