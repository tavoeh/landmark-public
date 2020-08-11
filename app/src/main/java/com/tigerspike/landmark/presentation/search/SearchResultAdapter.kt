package com.tigerspike.landmark.presentation.search

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tigerspike.landmark.databinding.ItemSearchResultBinding
import com.tigerspike.landmark.util.extension.inflater

/**
 * Created by Gustavo Enriquez on 26/7/20.
 *
 * List Adapter that holds the Note Search results data
 **/

class SearchResultAdapter : ListAdapter<SearchViewModel.Item, NewsItemHolder>(NewsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemHolder {
        val itemBinding = ItemSearchResultBinding.inflate(parent.inflater, parent, false)
        return NewsItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: NewsItemHolder, position: Int) {
        holder.binding.item = currentList[position]
    }
}

class NewsItemHolder(val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root)

object NewsDiffCallback : DiffUtil.ItemCallback<SearchViewModel.Item>() {
    override fun areItemsTheSame(oldItem: SearchViewModel.Item, newItem: SearchViewModel.Item) = newItem.id == oldItem.id
    override fun areContentsTheSame(oldItem: SearchViewModel.Item, newItem: SearchViewModel.Item) = newItem == oldItem
}