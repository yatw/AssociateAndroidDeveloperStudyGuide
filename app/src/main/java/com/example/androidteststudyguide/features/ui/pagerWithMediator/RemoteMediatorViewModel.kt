package com.example.androidteststudyguide.features.ui.pagerWithMediator

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import androidx.savedstate.SavedStateRegistryOwner
import com.example.androidteststudyguide.features.db.Cat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RemoteMediatorViewModel(
    private val repository: RemoteMediatorRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    val catBreedPagingDataFlow: Flow<PagingData<CatItem>> =
        repository.getCatBreed()
            .map { pagingData: PagingData<Cat> -> pagingData.map {
                    CatItem.CatBreedInfo(it.id, it.name, it.description, it.image)
                }
            }
            .map { it: PagingData<CatItem.CatBreedInfo> ->
                it.insertSeparators { before, after ->
                    if (after == null) {
                        // we're at the end of the list
                        return@insertSeparators null
                    }

                    if (before == null) {
                        // we're at the beginning of the list
                        return@insertSeparators CatItem.SeparatorItem("${after.name.first()}")
                    }

                    // check between 2 items
                    if (before.name.first() != after.name.first()) {
                        CatItem.SeparatorItem("${after.name.first()}")
                    } else {
                        // no separator
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)


    sealed class CatItem {
        data class CatBreedInfo(val id: String, val name: String, val description: String, val url: String?) : CatItem()
        data class SeparatorItem(val letter: String) : CatItem()
    }

    class Factory(
        owner: SavedStateRegistryOwner,
        private val repository: RemoteMediatorRepository
    ) : AbstractSavedStateViewModelFactory(owner, null) {

        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            if (modelClass.isAssignableFrom(RemoteMediatorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RemoteMediatorViewModel(repository, handle) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}