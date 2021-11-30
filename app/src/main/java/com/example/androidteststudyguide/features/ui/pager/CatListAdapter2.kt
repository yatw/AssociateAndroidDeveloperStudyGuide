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
import com.example.androidteststudyguide.features.db.Cat
import com.example.androidteststudyguide.features.ui.pagerWithMediator.RemoteMediatorViewModel
import timber.log.Timber


class CatListAdapter2 : PagingDataAdapter<RemoteMediatorViewModel.CatItem, RecyclerView.ViewHolder>(UIMODEL_COMPARATOR) {


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

            is RemoteMediatorViewModel.CatItem.SeparatorItem -> R.layout.item_cat_seperator_view
            else -> R.layout.item_cat
        }
    }


    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        val item: RemoteMediatorViewModel.CatItem? = getItem(position)


        when (item){
            is RemoteMediatorViewModel.CatItem.CatBreedInfo -> {
                (holder as CatBreedViewHolder).bind(item)
            }
            is RemoteMediatorViewModel.CatItem.SeparatorItem -> {
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

        fun bind(cat: RemoteMediatorViewModel.CatItem.CatBreedInfo?){
            if (cat != null){
                labelView.text = cat.name

                if (cat.url != null){
                    Glide.with(imageView.context).load(cat.url).into(imageView)
                }else{
                    Timber.i("${cat.name} doesn't have image")
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
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<RemoteMediatorViewModel.CatItem>() {
            override fun areItemsTheSame(oldItem: RemoteMediatorViewModel.CatItem, newItem: RemoteMediatorViewModel.CatItem): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItem: RemoteMediatorViewModel.CatItem, newItem: RemoteMediatorViewModel.CatItem): Boolean =
                oldItem == newItem
        }
    }
}