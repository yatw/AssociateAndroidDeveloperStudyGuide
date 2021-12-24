package com.example.androidteststudyguide.features.ui.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidteststudyguide.R
import com.example.androidteststudyguide.databinding.ItemCatBinding
import com.example.androidteststudyguide.databinding.ItemCatSeperatorViewBinding
import com.example.androidteststudyguide.features.api.CatBreedResponse
import timber.log.Timber
import java.lang.IllegalStateException


class CatListAdapter : PagingDataAdapter<PagerViewModel.CatItem, RecyclerView.ViewHolder>(UIMODEL_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_cat) {
            return CatBreedViewHolder(ItemCatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return SeparatorViewHolder(ItemCatSeperatorViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
//            is PagerViewModel.CatItem.SeparatorItem -> R.layout.item_cat_seperator_view
//            is PagerViewModel.CatItem.CatBreedInfo -> R.layout.item_cat
//            null -> throw UnsupportedOperationException("Unknown view")

            is PagerViewModel.CatItem.SeparatorItem -> R.layout.item_cat_seperator_view
            else -> R.layout.item_cat
        }
    }


    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        val item: PagerViewModel.CatItem? = getItem(position)
        when (item){
            is PagerViewModel.CatItem.CatBreedInfo -> {
                (holder as CatBreedViewHolder).bind(item.response)
            }
            is PagerViewModel.CatItem.SeparatorItem -> {
                (holder as SeparatorViewHolder).bind(item.letter)
            }

            // place holder
            else -> {
                (holder as CatBreedViewHolder).bind(null)
            }
        }
    }

    inner class CatBreedViewHolder(binding: ItemCatBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.imageView
        val labelView = binding.label

        fun bind(response: CatBreedResponse?){
            if (response != null){
                labelView.text = response.name

                if (response.image != null){
                    Glide.with(imageView.context).load(response.image.url).into(imageView)
                }else{
                    Timber.i("${response.name} doesn't have image")
                    Glide.with(imageView.context).load(R.drawable.ic_cloud_close).into(imageView)
                }

            }else{
                labelView.text = "Place Holder #$position"
                Glide.with(imageView.context).load(R.drawable.ic_cloud_close).into(imageView)
            }
        }
    }

    inner class SeparatorViewHolder(binding: ItemCatSeperatorViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val seperatorText = binding.separatorDescription
        fun bind(letter: String){
            seperatorText.text = letter
        }
    }

//    inner class PlaceHolderViewHolder(binding: ItemCatBinding) : RecyclerView.ViewHolder(binding.root) {
//        val seperatorText = binding.label
//        val imageView = binding.imageView
//        fun bind(){
//            seperatorText.text = "Unknown"
//            Glide.with(imageView.context).load(R.drawable.ic_cloud_close).into(imageView)
//        }
//    }


    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<PagerViewModel.CatItem>() {
            override fun areItemsTheSame(oldItem: PagerViewModel.CatItem, newItem: PagerViewModel.CatItem): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItem: PagerViewModel.CatItem, newItem: PagerViewModel.CatItem): Boolean =
                oldItem == newItem
        }
    }
}