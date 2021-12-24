package com.example.androidteststudyguide.features.ui.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.paging.LoadState
import com.example.androidteststudyguide.databinding.FragmentPagerBinding
import com.example.androidteststudyguide.features.api.CatApiService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *
 * Using pager library
 */
class PagerFragment: Fragment() {

    private lateinit var binding: FragmentPagerBinding


    // get the view model
    val viewModel: PagerViewModel by lazy {
        ViewModelProvider(this,PagerViewModel.Factory(this, PagerRepository(
            CatApiService.create())))
            .get(PagerViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CatListAdapter()
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = HeaderFooterAdapter{ adapter.retry()},
            footer = HeaderFooterAdapter{ adapter.retry()}
        )

        val pagingData = viewModel.catBreedPagingDataFlow
        lifecycleScope.launch {
            pagingData.collectLatest(adapter::submitData)
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect{ loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                // show empty list
                binding.emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds.
                binding.list.isVisible = !isListEmpty
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}