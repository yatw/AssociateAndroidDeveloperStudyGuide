package com.example.androidteststudyguide.features.ui.pagerWithMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.androidteststudyguide.features.api.CatApiService
import com.example.androidteststudyguide.features.api.CatBreedResponse
import com.example.androidteststudyguide.features.db.Cat
import com.example.androidteststudyguide.features.db.CatRemoteKeys
import com.example.androidteststudyguide.features.db.MyDatabase
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class CatRemoteMediator(
    private val service: CatApiService,
    private val database: MyDatabase
): RemoteMediator<Int, Cat>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Cat>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 0
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {

                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val cats: List<CatBreedResponse> = service.getCatImages(page, state.config.pageSize)

            val endOfPaginationReached = cats.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.catRemoteKeysDao().clearRemoteKeys()
                    database.catDao().clearCats()
                }
                val prevKey = if (page == 0) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = cats.map {
                    CatRemoteKeys(catId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.catRemoteKeysDao().insertAll(keys)


                val catEntityList = cats.map {
                    Cat(it.id, it.name, it.description, it.image?.url)
                }
                database.catDao().insertAll(catEntityList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Cat>): CatRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                database.catRemoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Cat>): CatRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                database.catRemoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Cat>
    ): CatRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.catRemoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}