package com.example.androidteststudyguide.features.ui.pagerWithMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.androidteststudyguide.features.api.CatApiService
import com.example.androidteststudyguide.features.api.CatBreedResponse
import com.example.androidteststudyguide.features.db.Cat
import com.example.androidteststudyguide.features.db.MyDatabase
import com.example.androidteststudyguide.features.ui.pager.*
import kotlinx.coroutines.flow.Flow


/**
 * Exact same as PagerFragment,
 * only different is passing in different repository into the viewModel
 */
class RemoteMediatorRepository(
    private val service: CatApiService,
    private val database: MyDatabase
) {


    fun getCatBreed(): Flow<PagingData<Cat>> {


        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5,
                enablePlaceholders = true),
            remoteMediator = CatRemoteMediator(
                service,
                database
            ),
            pagingSourceFactory = { database.catDao().getCats() }
        ).flow

    }
}
