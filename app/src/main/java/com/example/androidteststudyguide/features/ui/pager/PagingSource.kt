package com.example.androidteststudyguide.features.ui.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidteststudyguide.features.api.CatApiService
import com.example.androidteststudyguide.features.api.CatBreedResponse
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


/**
 * Paging Key is Int
 */
class PagingSource(
    private val service: CatApiService
) : PagingSource<Int, CatBreedResponse>() {

    /**
     * The load() function will be called by the Paging library to asynchronously fetch more data to be displayed as the user scrolls around.
     * The LoadParams object keeps information related to the load operation, including the following:
     *
     * Key of the page to be loaded. If this is the first time that load is called, LoadParams.key will be null. In this case, you will have to define the initial page key.
     *
     * Load size - the requested number of items to load.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatBreedResponse> {

        val page = params.key ?: 0
        return try {
            Timber.i("loading with page $page")

            val response: List<CatBreedResponse> = service.getCatImages(
                page = page,
                limit = params.loadSize, // how big each page
             )
            Timber.i("loaded ${response.size}")

            val nextKey = if (response.isEmpty()) {
                null
            } else {
                page + 1
            }
            LoadResult.Page(
                data = response,
                prevKey = if (page == 0) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    /**
     * A refresh happens whenever the Paging library wants to load new data to replace the current list,
     *
     * e.g., on swipe to refresh or on invalidation due to database updates,
     * config changes, process death, etc.
     *
     * Typically, subsequent refresh calls will want to restart loading data centered around PagingState.anchorPosition which represents the most recently accessed index.
     *
     * If return null, paging library will return user to the top of the list.
     *
     * https://stackoverflow.com/a/69649194/5777189
     */
    override fun getRefreshKey(state: PagingState<Int, CatBreedResponse>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}