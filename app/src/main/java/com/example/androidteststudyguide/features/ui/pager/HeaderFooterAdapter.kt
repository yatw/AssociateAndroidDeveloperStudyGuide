package com.example.androidteststudyguide.features.ui.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidteststudyguide.R
import com.example.androidteststudyguide.databinding.ItemListFooterBinding


class HeaderFooterAdapter(private val retry: () -> Unit) : LoadStateAdapter<HeaderFooterAdapter.FooterViewHolder>() {

    inner class FooterViewHolder(
        private val binding: ItemListFooterBinding,
        retry: () -> Unit
    ): RecyclerView.ViewHolder(binding.root){

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState){
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }
    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_footer, parent, false)
        val binding = ItemListFooterBinding.bind(view)
        return FooterViewHolder(binding, retry)
    }
}
