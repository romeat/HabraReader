package com.rprikhodko.habrareader.categories.hubs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rprikhodko.habrareader.Utils.Companion.toStringWithThousands
import com.rprikhodko.habrareader.categories.authors.AuthorAdapter
import com.rprikhodko.habrareader.categories.authors.data.AuthorPreview
import com.rprikhodko.habrareader.categories.hubs.data.HubPreview
import com.rprikhodko.habrareader.databinding.HubItemBinding

class HubAdapter : PagingDataAdapter<HubPreview, RecyclerView.ViewHolder>(PostDiffCallback()) {

    class PostDiffCallback : DiffUtil.ItemCallback<HubPreview>() {
        override fun areItemsTheSame(oldItem: HubPreview, newItem: HubPreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HubPreview, newItem: HubPreview): Boolean {
            return oldItem == newItem
        }
    }

    class HubViewHolder(private val binding: HubItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HubPreview) {
            with(binding) {
                title.text = item.title
                description.text = item.description
                rating.text = item.statistics.rating.toString()
                subscribers.text = item.statistics.subscribersCount.toStringWithThousands
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hub = getItem(position)
        hub?.let { (holder as HubViewHolder).bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HubViewHolder(
            HubItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}