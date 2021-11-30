package com.example.androidteststudyguide.features.ui.pager

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import androidx.savedstate.SavedStateRegistryOwner
import com.example.androidteststudyguide.features.api.CatBreedResponse
import kotlinx.coroutines.flow.*


class PagerViewModel(
    private val repository: PagerRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    val catBreedPagingDataFlow: Flow<PagingData<CatItem>> =
        repository.getCatBreed()
            .map { pagingData: PagingData<CatBreedResponse> -> pagingData.map { CatItem.CatBreedInfo(it) } }
            .map { it: PagingData<CatItem.CatBreedInfo> ->
                it.insertSeparators { before, after ->
                    if (after == null) {
                        // we're at the end of the list
                        return@insertSeparators null
                    }

                    if (before == null) {
                        // we're at the beginning of the list
                        return@insertSeparators CatItem.SeparatorItem("${after.response.name.first()}")
                    }

                    // check between 2 items
                    if (before.response.name.first() != after.response.name.first()) {
                        CatItem.SeparatorItem("${after.response.name.first()}")
                    } else {
                        // no separator
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)


    sealed class CatItem {
        data class CatBreedInfo(val response: CatBreedResponse) : CatItem()
        data class SeparatorItem(val letter: String) : CatItem()
       // class PlaceHolderItem(): CatItem()
    }

    class Factory(
        owner: SavedStateRegistryOwner,
        private val repository: PagerRepository
    ) : AbstractSavedStateViewModelFactory(owner, null) {

        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            if (modelClass.isAssignableFrom(PagerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PagerViewModel(repository, handle) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}