package com.example.androidteststudyguide.features.ui.pager

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.androidteststudyguide.features.api.CatApiService
import com.example.androidteststudyguide.features.api.CatBreedResponse
import kotlinx.coroutines.flow.Flow

class PagerRepository(private val service: CatApiService) {


    fun getCatBreed(): Flow<PagingData<CatBreedResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = true,
                maxSize = 15
            ),
            pagingSourceFactory = { PagingSource(service) }
        ).flow
    }
}
